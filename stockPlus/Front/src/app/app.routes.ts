/**
 * Configuração de Rotas da Aplicação
 */

import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { CadastroComponent } from './pages/cadastro/cadastro.component';
import { EstoquesComponent } from './pages/estoques/estoques.component';
import { EstoqueComponent } from './pages/estoque/estoque.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/estoques',
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
    path: 'estoques',
    component: EstoquesComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'estoque/:id',
    component: EstoqueComponent,
    canActivate: [AuthGuard],
  },
  {
    path: '**',
    redirectTo: '/estoques',
  },
];
