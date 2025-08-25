import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PointService} from '../../services/point.service';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-graph',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './graph.component.html',
  styleUrl: './graph.component.css'
})
export class GraphComponent implements OnInit {
  @Input() R: number | null = null;

  points: any[] = [];

  constructor(private pointService: PointService) {}

  @Output() notificationChanged = new EventEmitter<string | null>();
  notification: string | null = null;

  ngOnInit() {
    this.pointService.pointsSubject.subscribe(points => {
      this.points = points;
    });
  }

  onClick(event: MouseEvent) {
    if (this.validateR(this.R)) {
      this.notification = null;
      const x = (event.offsetX - 200) / (75 / ((this.R ?? 1) / 2));
      const y = (-event.offsetY + 200) / (75 / ((this.R ?? 1) / 2));
      this.pointService.checkPoint(x, y, this.R ?? 0.0).subscribe(
        (point) => {
          this.pointService.addPoint(point);
        },
        (error) => {
          this.notification = error.error;
          this.notificationChanged.emit(this.notification);
        }
      );
    }
    this.notificationChanged.emit(this.notification);
  }

  validateR(R: number | null): boolean {
    if (R === null) {
      this.notification = "Выберите значение R"
      return false;
    }
    return true;
  }

  isInArea(x: number, y: number, r: number): boolean {
    const firstCond = (x <= 0 && y >= 0 && Math.pow(y, 2) <= Math.pow(r / 2, 2) - Math.pow(x, 2));
    const secondCond = (x <= 0 && y <= 0 && y >= -2 * x - r);
    const thirdCond = (x >= 0 && y <= 0 && x <= r / 2 && y >= -r);
    return firstCond || secondCond || thirdCond;
  }
}
