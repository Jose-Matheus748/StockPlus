import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { TipoUsuario, Usuario } from '../../models';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.scss'],
})
export class CadastroComponent implements OnInit {
  nome: string = '';
  email: string = '';
  cpfOuCnpj: string = '';
  senha: string = '';
  confirmarSenha: string = '';
  erro: string = '';
  sucesso: string = '';
  isLoading: boolean = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  handleSubmit(): void {
    this.erro = '';
    this.sucesso = '';
    this.isLoading = true;

    // Validações
    if (!this.nome || !this.email || !this.cpfOuCnpj || !this.senha || !this.confirmarSenha) {
      this.erro = 'Por favor, preencha todos os campos';
      this.isLoading = false;
      return;
    }

    if (!this.email.includes('@')) {
      this.erro = 'Por favor, insira um email válido';
      this.isLoading = false;
      return;
    }

    if (this.senha !== this.confirmarSenha) {
      this.erro = 'As senhas não correspondem';
      this.isLoading = false;
      return;
    }

    if (this.senha.length < 6) {
      this.erro = 'A senha deve ter pelo menos 6 caracteres';
      this.isLoading = false;
      return;
    }

    const novoUsuario: Usuario = {
      nome: this.nome,
      email: this.email,
      cpfOuCnpj: this.cpfOuCnpj,
      senha: this.senha,
      tipo: TipoUsuario.FUNCIONARIO,
    };

    this.authService.register(novoUsuario).subscribe({
      next: () => {
        this.sucesso = 'Cadastro realizado com sucesso! Redirecionando para login...';

        setTimeout(() => {
          const loginCredentials = {
            email: this.email,
            senha: this.senha,
          };

          this.authService.login(loginCredentials).subscribe({
            next: () => {
              this.router.navigate(['/estoque']);
            },
            error: () => {
              this.router.navigate(['/login']);
            },
          });
        }, 1500);
      },
      error: (error) => {
        this.erro = error.error?.message || 'Erro ao registrar. Tente novamente.';
        this.isLoading = false;
      },
    });
  }
}
