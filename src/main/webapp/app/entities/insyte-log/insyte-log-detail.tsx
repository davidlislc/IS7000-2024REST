import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './insyte-log.reducer';

export const InsyteLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const insyteLogEntity = useAppSelector(state => state.insyteLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="insyteLogDetailsHeading">Insyte Log</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{insyteLogEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{insyteLogEntity.name}</dd>
          <dt>
            <span id="activity">Activity</span>
          </dt>
          <dd>{insyteLogEntity.activity}</dd>
          <dt>
            <span id="rundate">Rundate</span>
          </dt>
          <dd>
            {insyteLogEntity.rundate ? <TextFormat value={insyteLogEntity.rundate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>User</dt>
          <dd>{insyteLogEntity.user ? insyteLogEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/insyte-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/insyte-log/${insyteLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default InsyteLogDetail;
