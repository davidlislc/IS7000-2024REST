import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './batch.reducer';

export const BatchDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const batchEntity = useAppSelector(state => state.batch.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="batchDetailsHeading">Batch</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{batchEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{batchEntity.name}</dd>
          <dt>
            <span id="job">Job</span>
          </dt>
          <dd>{batchEntity.job}</dd>
          <dt>
            <span id="rundate">Rundate</span>
          </dt>
          <dd>{batchEntity.rundate ? <TextFormat value={batchEntity.rundate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="batchstatus">Batchstatus</span>
          </dt>
          <dd>{batchEntity.batchstatus}</dd>
          <dt>User</dt>
          <dd>{batchEntity.user ? batchEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/batch" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/batch/${batchEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default BatchDetail;
