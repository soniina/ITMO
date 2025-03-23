import { Component } from '@angular/core';
import {ReactiveFormsModule, FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {Password, PasswordDirective} from 'primeng/password';
import {InputText} from 'primeng/inputtext';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf,
    Password,
    PasswordDirective,
    InputText
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  error: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login(this.username, this.password).subscribe(
      (response) => {
        this.authService.saveToken(response.token);
        this.router.navigate(['/main']);
      },
      (error) => {
        if (error.status != 0) {
          this.error = error.error;
        } else {
          this.error = error.message;
        }
      }
    );
  }

  register(): void {
    this.authService.register(this.username, this.password).subscribe(
      (response) => {
        this.authService.saveToken(response.token);
        this.router.navigate(['/main']);
      },
      (error) => {
        this.error = error.error;
      }
    );
  }
}
