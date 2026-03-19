package com.example.empleados.controller;

import com.example.empleados.controller.dto.DepartamentoDtos;
import com.example.empleados.service.DepartamentoService;
import com.example.empleados.service.InvalidPaginationException;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departamentos")
public class DepartamentoController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;
    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "nombre");

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping
    public Page<DepartamentoDtos.DepartamentoResponse> list(
        @RequestParam(name = "page", required = false) Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "20") Integer size,
        @RequestParam(name = "sort", required = false) String[] sort
    ) {
        List<String> sortParameters = sort == null ? List.of() : List.of(sort);
        validatePagination(page, size, sortParameters);
        Pageable pageable = PageRequest.of(page, size, parseSort(sortParameters));
        return departamentoService.findAll(pageable)
            .map(departamento -> new DepartamentoDtos.DepartamentoResponse(departamento.getId(), departamento.getNombre()));
    }

    @GetMapping("/{id}")
    public DepartamentoDtos.DepartamentoResponse getById(@PathVariable Long id) {
        var departamento = departamentoService.findById(id);
        return new DepartamentoDtos.DepartamentoResponse(departamento.getId(), departamento.getNombre());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartamentoDtos.DepartamentoResponse create(@Valid @RequestBody DepartamentoDtos.DepartamentoRequest request) {
        var created = departamentoService.create(request);
        return new DepartamentoDtos.DepartamentoResponse(created.getId(), created.getNombre());
    }

    @PutMapping("/{id}")
    public DepartamentoDtos.DepartamentoResponse update(
        @PathVariable Long id,
        @Valid @RequestBody DepartamentoDtos.DepartamentoRequest request
    ) {
        var updated = departamentoService.update(id, request);
        return new DepartamentoDtos.DepartamentoResponse(updated.getId(), updated.getNombre());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        departamentoService.delete(id);
    }

    private void validatePagination(Integer page, Integer size, List<String> sort) {
        if (page == null) {
            throw new InvalidPaginationException("El parámetro 'page' es obligatorio");
        }
        if (sort == null || sort.isEmpty()) {
            throw new InvalidPaginationException("El parámetro 'sort' es obligatorio");
        }
        if (page < 0) {
            throw new InvalidPaginationException("El parámetro 'page' debe ser mayor o igual a 0");
        }

        int pageSize = size == null ? DEFAULT_PAGE_SIZE : size;
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new InvalidPaginationException("El parámetro 'size' debe estar entre 1 y 100");
        }

        parseSort(sort);
    }

    private Sort parseSort(List<String> sort) {
        List<String> normalizedSort = normalizeSort(sort);
        Sort result = Sort.unsorted();

        for (String sortParam : normalizedSort) {
            String[] parts = sortParam.split(",");
            if (parts.length != 2) {
                throw new InvalidPaginationException("Formato inválido en 'sort'. Use campo,direccion");
            }

            String field = parts[0].trim();
            String direction = parts[1].trim().toLowerCase();
            if (!ALLOWED_SORT_FIELDS.contains(field)) {
                throw new InvalidPaginationException("Campo de ordenamiento no permitido: " + field);
            }
            if (!"asc".equals(direction) && !"desc".equals(direction)) {
                throw new InvalidPaginationException("Dirección de ordenamiento no permitida: " + direction);
            }

            Sort.Order order = new Sort.Order("desc".equals(direction) ? Sort.Direction.DESC : Sort.Direction.ASC, field);
            result = result.and(Sort.by(order));
        }

        return result;
    }

    private List<String> normalizeSort(List<String> sort) {
        boolean hasCombinedFormat = sort.stream().anyMatch(entry -> entry.contains(","));
        if (hasCombinedFormat) {
            return sort;
        }

        if (sort.size() % 2 != 0) {
            throw new InvalidPaginationException("Formato inválido en 'sort'. Use campo,direccion");
        }

        List<String> normalized = new ArrayList<>();
        for (int index = 0; index < sort.size(); index += 2) {
            normalized.add(sort.get(index) + "," + sort.get(index + 1));
        }

        return normalized;
    }
}
