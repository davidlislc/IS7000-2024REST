import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './giftcard.reducer';

export const GiftcardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const giftcardEntity = useAppSelector(state => state.giftcard.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="giftcardDetailsHeading">Giftcard</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{giftcardEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{giftcardEntity.name}</dd>
          <dt>
            <span id="giftcardamount">Giftcardamount</span>
          </dt>
          <dd>{giftcardEntity.giftcardamount}</dd>
          <dt>
            <span id="addDate">Add Date</span>
          </dt>
          <dd>
            {giftcardEntity.addDate ? <TextFormat value={giftcardEntity.addDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{giftcardEntity.user ? giftcardEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/giftcard" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/giftcard/${giftcardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GiftcardDetail;
