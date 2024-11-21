import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { INDEX } from 'app/shared/model/enumerations/index.model';
import { createEntity, getEntity, reset, updateEntity } from './market-overview.reducer';

export const MarketOverviewUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const marketOverviewEntity = useAppSelector(state => state.marketOverview.entity);
  const loading = useAppSelector(state => state.marketOverview.loading);
  const updating = useAppSelector(state => state.marketOverview.updating);
  const updateSuccess = useAppSelector(state => state.marketOverview.updateSuccess);
  const iNDEXValues = Object.keys(INDEX);

  const handleClose = () => {
    navigate(`/market-overview${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.change !== undefined && typeof values.change !== 'number') {
      values.change = Number(values.change);
    }

    const entity = {
      ...marketOverviewEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ticker: 'SPY',
          ...marketOverviewEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="is70002024App.marketOverview.home.createOrEditLabel" data-cy="MarketOverviewCreateUpdateHeading">
            Create or edit a Market Overview
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="market-overview-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Name"
                id="market-overview-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Price"
                id="market-overview-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                label="Change"
                id="market-overview-change"
                name="change"
                data-cy="change"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Ticker" id="market-overview-ticker" name="ticker" data-cy="ticker" type="select">
                {iNDEXValues.map(iNDEX => (
                  <option value={iNDEX} key={iNDEX}>
                    {iNDEX}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Marketdate"
                id="market-overview-marketdate"
                name="marketdate"
                data-cy="marketdate"
                type="date"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/market-overview" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MarketOverviewUpdate;
