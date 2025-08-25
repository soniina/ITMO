import {Component, OnInit} from '@angular/core';
import {TableModule} from 'primeng/table';
import {NgForOf} from '@angular/common';
import {PointService} from '../../services/point.service';

@Component({
  selector: 'app-results-table',
  standalone: true,
  imports: [
    TableModule,
    NgForOf
  ],
  templateUrl: './results-table.component.html',
  styleUrl: './results-table.component.css'
})

export class ResultsTableComponent implements OnInit {
  points: any[] = [];
  cols: any[] = [
    { field: 'x', header: 'X' },
    { field: 'y', header: 'Y' },
    { field: 'r', header: 'R' },
    { field: 'inArea', header: 'Попадание' }
  ];

  constructor(private pointService: PointService) {}

  ngOnInit() {
    this.pointService.pointsSubject.subscribe(points => {
      this.points = points;
    });
  }

}
