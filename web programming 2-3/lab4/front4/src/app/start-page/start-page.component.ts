import { Component } from '@angular/core';
import {HeaderComponent} from '../shared/header/header.component';
import {FooterComponent} from '../shared/footer/footer.component';
import {LoginComponent} from './login/login.component';

@Component({
  selector: 'app-start-page',
  standalone: true,
  imports: [
    HeaderComponent,
    FooterComponent,
    LoginComponent
  ],
  templateUrl: './start-page.component.html',
  styleUrl: './start-page.component.css'
})
export class StartPageComponent {

}
