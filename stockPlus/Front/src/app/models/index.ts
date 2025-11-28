/**
 * Modelos TypeScript para as entidades da aplicação
 */

export enum TipoUsuario {
  ADMIN = 'ADMIN',
  FUNCIONARIO = 'FUNCIONARIO',
}

export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  cpfOuCnpj: string;
  tipo: TipoUsuario;
  senha?: string;
}

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface Estoque {
  id?: number;
  nome: string;
  descricao?: string;
  produtos?: Produto[];
}

export interface Produto {
  id?: number;
  nome: string;
  descricao?: string;
  fornecedor: string;
  marca: string;
  quantidade: number;
  precoUnitario: number;
  estoqueId?: number;
}

export interface ValorTotalEstoque {
  valorTotal: number;
  quantidadeTotal: number;
}

export interface AuthResponse {
  usuario: Usuario;
  token?: string;
}
