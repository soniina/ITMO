import { Component } from '@angular/core';
import {InputFormComponent} from './input-form/input-form.component';
import {GraphComponent} from './graph/graph.component';
import {ResultsTableComponent} from './results-table/results-table.component';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [
    InputFormComponent,
    GraphComponent,
    ResultsTableComponent,
    NgIf
  ],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})

export class MainPageComponent {
  R: number | null = null;

  onRChanged(event: { R: number | null }) {
    this.R = event.R;
  }

  notification: string | null = null;

  onNotificationChange(message: string | null) {
    this.notification = message;
  }
}
