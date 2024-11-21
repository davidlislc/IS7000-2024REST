import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Subscriptions from './subscriptions';
import SubscriptionsDetail from './subscriptions-detail';
import SubscriptionsUpdate from './subscriptions-update';
import SubscriptionsDeleteDialog from './subscriptions-delete-dialog';

const SubscriptionsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Subscriptions />} />
    <Route path="new" element={<SubscriptionsUpdate />} />
    <Route path=":id">
      <Route index element={<SubscriptionsDetail />} />
      <Route path="edit" element={<SubscriptionsUpdate />} />
      <Route path="delete" element={<SubscriptionsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubscriptionsRoutes;
