import { inject } from "@angular/core";
import {CanActivateFn, Router} from '@angular/router';
import {AuthService} from './auth.service';

export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  console.log('authGuard', auth.estConnecte());
  return auth.estConnecte()
    ? true
    : router.parseUrl('/login?retour=' + router.url);
};
