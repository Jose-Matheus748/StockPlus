import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { CadastroComponent } from './pages/cadastro/cadastro.component';
import { EstoquesComponent } from './pages/estoques/estoques.component';
import { EstoqueComponent } from './pages/estoque/estoque.component';
import { AuthGuard } from './guards/auth.guard';
import { ProdutosComponent } from './pages/produtos/produtos.component';
import { ProtocolosComponent } from './pages/protocolos/protocolos.component';
import { ProtocoloComponent } from './pages/protocolo/protocolo.component';

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
  path: 'produtos',
  component: ProdutosComponent,
  },
  {
    path: 'protocolos',
    component: ProtocolosComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'protocolo/:id',
    component: ProtocoloComponent,
    canActivate: [AuthGuard],
  },
  {
    path: '**',
    redirectTo: '/estoques',
  },
];
