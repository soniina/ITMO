import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';

interface Point {
  x: number;
  y: number;
  r: number;
  result: string;
}

@Injectable({
  providedIn: 'root'
})

export class PointService {
  pointsSubject = new BehaviorSubject<Point[]>([]);

  private apiUrl = 'http://localhost:8080/server/api/point';

  constructor(private http: HttpClient) {
    this.loadPoints()
  }

  loadPoints() {
    this.http.get<Point[]>(`${this.apiUrl}/all`).subscribe({
      next: (points) => {
        this.pointsSubject.next(points);
      }
    })
  }

  checkPoint(x: number, y: number, r: number): Observable<any> {
    return this.http.get(`${this.apiUrl}?x=${x}&y=${y}&r=${r}`);
  }

  addPoint(newPoint: Point) {
    this.pointsSubject.next([...this.pointsSubject.value, newPoint]);
  }

}
