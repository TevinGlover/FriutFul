import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/parents">
        Parents
      </MenuItem>
      <MenuItem icon="asterisk" to="/child">
        Child
      </MenuItem>
      <MenuItem icon="asterisk" to="/balance">
        Balance
      </MenuItem>
      <MenuItem icon="asterisk" to="/transactions">
        Transactions
      </MenuItem>
      <MenuItem icon="asterisk" to="/points">
        Points
      </MenuItem>
      <MenuItem icon="asterisk" to="/task">
        Task
      </MenuItem>
      <MenuItem icon="asterisk" to="/achivement">
        Achivement
      </MenuItem>
      <MenuItem icon="asterisk" to="/credit-score">
        Credit Score
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
