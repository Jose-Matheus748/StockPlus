/**
 * Configuração da API do backend Spring Boot
 */

export const API_CONFIG = {
  baseURL: 'http://localhost:8080',
  timeout: 30000,
};

export const API_ENDPOINTS = {
  // Autenticação
  auth: {
    login: '/auth/login',
  },

  // Usuários
  usuarios: {
    create: '/usuarios',
    getAll: '/usuarios',
    getById: (id: number) => `/usuarios/${id}`,
    update: (id: number) => `/usuarios/${id}`,
    delete: (id: number) => `/usuarios/${id}`,
  },

  // Estoques
  estoques: {
    create: '/estoques',
    getAll: '/estoques',
    getById: (id: number) => `/estoques/${id}`,
    update: (id: number) => `/estoques/${id}`,
    delete: (id: number) => `/estoques/${id}`,
    listarProdutos: (id: number) => `/estoques/${id}/produtos`,
    calcularValorTotal: (id: number) => `/estoques/${id}/valor-total`,

    // 🔥 ROTA QUE FALTAVA para listar apenas os estoques do usuário autenticado
    meusEstoques: '/estoques/meus-estoques',
  },

  // Produtos
  produtos: {
    create: '/produtos',
    getAll: '/produtos',
    getById: (id: number) => `/produtos/${id}`,
    update: (id: number) => `/produtos/${id}`,
    delete: (id: number) => `/produtos/${id}`,
    addQuantidade: (id: number) => `/produtos/${id}/add`,
    removeQuantidade: (id: number) => `/produtos/${id}/remove`,
    getValorTotal: '/produtos/valor-total',
    listarPorUsuario: (usuarioId: number) => `/produtos/meus-produtos?usuarioId=${usuarioId}`,
  },
};
