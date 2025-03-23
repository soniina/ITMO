import {Component, EventEmitter, Output} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import {InputNumber} from 'primeng/inputnumber';
import {Select} from 'primeng/select';
import {RadioButton} from 'primeng/radiobutton';
import {PointService} from '../../services/point.service';
import {Slider} from 'primeng/slider';

interface RValue {
  label: string;
  value: number;
}

@Component({
  selector: 'app-input-form',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    InputTextModule,
    ButtonModule,
    InputNumber,
    Select,
    RadioButton,
    NgIf,
    Slider
  ],
  templateUrl: './input-form.component.html',
  styleUrls: ['./input-form.component.css']
})

export class InputFormComponent {

  selectedR: RValue | null = null;

  @Output() rChanged = new EventEmitter<{ R: number | null }>();

  onRChange() {
    this.rChanged.emit({ R: this.selectedR?.value || null });
  }

  xValues = [-3, -2, -1, 0, 1, 2, 3, 4, 5];
  selectedX: number | null = null;

  rValues = [
    { label: '1', value: 1 },
    { label: '2', value: 2 },
    { label: '3', value: 3 },
    { label: '4', value: 4 },
    { label: '5', value: 5 }
  ];

  yValue: number = -5;

  @Output() notificationChanged = new EventEmitter<string | null>();
  notification: string | null = null;

  constructor(private pointService: PointService) {}

  checkPoint() {
    if (this.validateX(this.selectedX) && this.validateR(this.selectedR)) {
      this.notification = null;
      this.pointService.checkPoint(this.selectedX ?? 0.0, this.yValue, this.selectedR?.value ?? 0.0).subscribe({
        next: (point) => {
          this.pointService.addPoint(point);
        },
        error: (err) => {
          this.notification = err.error;
        }
      })
    }
    this.notificationChanged.emit(this.notification);
  }

  validateX(x: number | null): boolean {
    if (x === null) {
      this.notification = "Выберите значение X"
      return false;
    }
    return true;
  }

  validateR(R: RValue | null): boolean {
    if (R === null) {
      this.notification = "Выберите значение R"
      return false;
    }
    return true;
  }

}
