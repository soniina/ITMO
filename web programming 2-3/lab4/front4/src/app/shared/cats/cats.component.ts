import {Component, HostListener, OnInit} from '@angular/core';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-cats',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './cats.component.html',
  styleUrl: './cats.component.css'
})
export class CatsComponent implements OnInit {

  rows: any[][] = [];
  cellHeight = window.innerHeight / 4;
  catTableRows = 4;

  ngOnInit(): void {
    this.createTable();
  }

  createTable(): void {
    for (let r = 0; r < this.catTableRows; r++) {
      this.createRow();
    }
  }

  createRow(): void {
    const row = [];
    for (let c = 0; c < 6; c++) {
      row.push(this.createCat());
    }
    this.rows.push(row);
  }

  createCat(): any {
    const randomCat = Math.floor(Math.random() * 28) + 1;
    return {
      src: `/assets/img/cat${randomCat}.png`,
      width: this.getRandomSize(300, 400),
      originalSrc: `/assets/img/cat${randomCat}.png`,
      yawnSrc: `/assets/img/cat${randomCat}_yawn.png`
    };
  }

  onMouseOver(cat: any): void {
    cat.src = cat.yawnSrc;
  }

  onMouseOut(cat: any): void {
    cat.src = cat.originalSrc;
  }

  getRandomSize(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }
}
