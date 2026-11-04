import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Brand } from '../model/brand.model';

@Component({
  selector: 'app-update-brand',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-brand.html',
})
export class UpdateBrandComponent {

  @Input() brand!: Brand;
  @Input() add!: boolean; // true = add mode, false = edit mode

  @Output() brandUpdated = new EventEmitter<Brand>();

  saveBrand(): void {
    this.brandUpdated.emit(this.brand);
  }
}
