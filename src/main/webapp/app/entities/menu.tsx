import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/subscriptions">
        Subscriptions
      </MenuItem>
      <MenuItem icon="asterisk" to="/service">
        Service
      </MenuItem>
      <MenuItem icon="asterisk" to="/transaction">
        Transaction
      </MenuItem>
      <MenuItem icon="asterisk" to="/wallet">
        Wallet
      </MenuItem>
      <MenuItem icon="asterisk" to="/batch">
        Batch
      </MenuItem>
      <MenuItem icon="asterisk" to="/insyte-log">
        Insyte Log
      </MenuItem>
      <MenuItem icon="asterisk" to="/market-sector">
        Market Sector
      </MenuItem>
      <MenuItem icon="asterisk" to="/market-overview">
        Market Overview
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
