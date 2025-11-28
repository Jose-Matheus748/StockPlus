import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';
import 'zone.js';  // Importa Zone.js para Angular

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
