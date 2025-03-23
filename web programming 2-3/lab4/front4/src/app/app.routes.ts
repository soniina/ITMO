import { Routes } from '@angular/router';
import {LoginComponent} from './start-page/login/login.component';
import {MainPageComponent} from './main-page/main-page.component';
import {AuthGuard} from './guards/auth.guard';

export const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'main', component: MainPageComponent, canActivate: [AuthGuard] },
    { path: '**', redirectTo: '/login' }
  ];
