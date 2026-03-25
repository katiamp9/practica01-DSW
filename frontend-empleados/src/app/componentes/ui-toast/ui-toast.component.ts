import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-ui-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ui-toast.component.html',
  styleUrl: './ui-toast.component.css'
})
export class UiToastComponent {
  @Input({ required: true }) message = '';
  @Input() variant: 'success' | 'error' | 'warning' = 'success';
  @Output() dismissed = new EventEmitter<void>();

  dismiss(): void {
    this.dismissed.emit();
  }
}
