import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Giftcard from './giftcard';
import GiftcardDetail from './giftcard-detail';
import GiftcardUpdate from './giftcard-update';
import GiftcardDeleteDialog from './giftcard-delete-dialog';

const GiftcardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Giftcard />} />
    <Route path="new" element={<GiftcardUpdate />} />
    <Route path=":id">
      <Route index element={<GiftcardDetail />} />
      <Route path="edit" element={<GiftcardUpdate />} />
      <Route path="delete" element={<GiftcardDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GiftcardRoutes;
