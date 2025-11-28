/**
 * Configuração de Rotas da Aplicação
 */

import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { CadastroComponent } from './pages/cadastro/cadastro.component';
import { EstoqueComponent } from './pages/estoque/estoque.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/estoque',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'cadastro',
    component: CadastroComponent,
  },
  {
    path: 'estoque',
    component: EstoqueComponent,
    canActivate: [AuthGuard],
  },
  {
    path: '**',
    redirectTo: '/estoque',
  },
];
