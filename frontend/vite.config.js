import { defineConfig } from 'vite';

export default defineConfig({
  root: 'src',
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/Libreria')
      }
    }
  },
  build: {
    outDir: '../dist',
    rollupOptions: {
      input: {
        main: 'index.html',
        login: 'pages/login.html',
        formulario: 'pages/formulario.html',
        documentos: 'pages/documentos.html',
        formularioDoc: 'pages/formularioDoc.html',
        descripcionDocumento: 'pages/descripcionDocumento.html',
        paginaPrincipal: 'pages/paginaPrincipal.html',
        informacionConsulta: 'pages/informacionConsulta.html',
        reservaConsulta: 'pages/reservaConsulta.html'
      }
    }
  }
});
