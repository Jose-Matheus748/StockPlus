import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { LayoutComponent } from '../../components/layout/layout.component';
import { ProtocoloService } from '../../services/protocolo.service';
import { AuthService } from '../../services/auth.service';
import { Protocolo } from '../../models';

@Component({
  selector: 'app-protocolos',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, LayoutComponent],
  templateUrl: './protocolos.component.html',
  styleUrls: ['./protocolos.component.scss']
})
export class ProtocolosComponent implements OnInit {

  protocolos: Protocolo[] = [];
  isLoading = true;
  erro = '';

  showFormModal = false;

  novoProtocolo: Protocolo = {
    nome: '',
    preco: 0,
    valorTotal: 0,
    itens: []
  };

  constructor(
    private protocoloService: ProtocoloService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarProtocolos();
  }

  carregarProtocolos(): void {
    this.isLoading = true;
    this.erro = '';

    const usuario = this.authService.getCurrentUser();
    if (!usuario || !usuario.id) {
      this.erro = 'Usuário não autenticado';
      this.isLoading = false;
      return;
    }

    this.protocoloService.listarPorUsuario(usuario.id).subscribe({
      next: data => {
        this.protocolos = data;
        this.isLoading = false;
      },
      error: err => {
        console.error(err);
        this.erro = 'Erro ao carregar protocolos';
        this.isLoading = false;
      }
    });
  }

  abrirFormulario(): void {
    this.novoProtocolo = {
      nome: '',
      preco: 0,
      valorTotal: 0,
      itens: []
    };
    this.showFormModal = true;
  }

  fecharFormulario(): void {
    this.showFormModal = false;
  }

  criarProtocolo(): void {
    if (!this.novoProtocolo.nome.trim()) {
      alert('Digite um nome para o protocolo.');
      return;
    }

    const usuario = this.authService.getCurrentUser();
    if (!usuario?.id) {
      alert('Usuário não autenticado');
      return;
    }

    const payload = {
      nome: this.novoProtocolo.nome,
      preco: this.novoProtocolo.preco,
      usuarioId: usuario.id
    };

    this.protocoloService.criar(payload).subscribe({
      next: () => {
        this.carregarProtocolos();
        this.fecharFormulario();
        alert('Protocolo criado com sucesso!');
      },
      error: err => {
        console.error(err);
        alert('Erro ao criar protocolo.');
      }
    });
  }

  acessarProtocolo(p: Protocolo) {
    if (p.id) {
      this.router.navigate(['/protocolo', p.id]);
    }
  }

  deletarProtocolo(p: Protocolo): void {
    if (!p.id) return;

    if (!confirm(`Excluir protocolo "${p.nome}"?`)) return;

    this.protocoloService.deletar(p.id).subscribe({
      next: () => {
        this.carregarProtocolos();
        alert('Protocolo deletado com sucesso!');
      },
      error: err => {
        console.error(err);
        alert('Erro ao deletar protocolo.');
      }
    });
  }
}
