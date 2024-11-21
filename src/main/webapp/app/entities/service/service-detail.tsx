import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { DurationFormat } from 'app/shared/DurationFormat';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './service.reducer';

export const ServiceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const serviceEntity = useAppSelector(state => state.service.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="serviceDetailsHeading">Service</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{serviceEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{serviceEntity.name}</dd>
          <dt>
            <span id="level">Level</span>
          </dt>
          <dd>{serviceEntity.level}</dd>
          <dt>
            <span id="interval">Interval</span>
          </dt>
          <dd>
            {serviceEntity.interval ? <DurationFormat value={serviceEntity.interval} /> : null} ({serviceEntity.interval})
          </dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{serviceEntity.price}</dd>
        </dl>
        <Button tag={Link} to="/service" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/service/${serviceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ServiceDetail;
