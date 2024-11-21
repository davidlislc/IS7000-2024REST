import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './market-overview.reducer';

export const MarketOverviewDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const marketOverviewEntity = useAppSelector(state => state.marketOverview.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="marketOverviewDetailsHeading">Market Overview</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{marketOverviewEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{marketOverviewEntity.name}</dd>
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{marketOverviewEntity.price}</dd>
          <dt>
            <span id="change">Change</span>
          </dt>
          <dd>{marketOverviewEntity.change}</dd>
          <dt>
            <span id="ticker">Ticker</span>
          </dt>
          <dd>{marketOverviewEntity.ticker}</dd>
          <dt>
            <span id="marketdate">Marketdate</span>
          </dt>
          <dd>
            {marketOverviewEntity.marketdate ? (
              <TextFormat value={marketOverviewEntity.marketdate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/market-overview" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/market-overview/${marketOverviewEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MarketOverviewDetail;
