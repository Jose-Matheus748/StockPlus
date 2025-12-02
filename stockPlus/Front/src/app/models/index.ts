export enum TipoUsuario {
  ADMIN = 'ADMIN',
  FUNCIONARIO = 'FUNCIONARIO',
}
export interface Estoque {
  id?: number;
  nome: string;
  descricao?: string;
  usuarioId?: number;
  produtos?: ProdutoEstoque[];
}

export interface Produto {
  id: number | null;
  nome: string;
  descricao?: string;
  fornecedor: string;
  marca: string;
  precoUnitario: number;
  usuarioId: number | null;
}


export interface ProdutoEstoque {
  id?: number | null;
  quantidade: number;

  produto: {
    id?: number | null;
    nome: string;
    fornecedor: string;
    marca: string;
    precoUnitario: number;
    descricao?: string;
    usuarioId?: number | null;
  };
}

export interface ValorTotalEstoque {
  valorTotal: number;
  quantidadeTotal: number;
}

export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  senha?: string;
  cpfOuCnpj: string;
  tipo: TipoUsuario;
}

export interface LoginRequest {
  email: string;
  senha: string;
}
