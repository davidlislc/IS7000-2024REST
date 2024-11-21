import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subscriptions.reducer';

export const SubscriptionsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subscriptionsEntity = useAppSelector(state => state.subscriptions.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subscriptionsDetailsHeading">Subscriptions</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{subscriptionsEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{subscriptionsEntity.name}</dd>
          <dt>
            <span id="subdate">Subdate</span>
          </dt>
          <dd>
            {subscriptionsEntity.subdate ? (
              <TextFormat value={subscriptionsEntity.subdate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{subscriptionsEntity.status}</dd>
          <dt>User</dt>
          <dd>{subscriptionsEntity.user ? subscriptionsEntity.user.login : ''}</dd>
          <dt>Service</dt>
          <dd>{subscriptionsEntity.service ? subscriptionsEntity.service.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/subscriptions" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscriptions/${subscriptionsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubscriptionsDetail;
