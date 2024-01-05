import parents from 'app/entities/parents/parents.reducer';
import child from 'app/entities/child/child.reducer';
import balance from 'app/entities/balance/balance.reducer';
import transactions from 'app/entities/transactions/transactions.reducer';
import points from 'app/entities/points/points.reducer';
import task from 'app/entities/task/task.reducer';
import achivement from 'app/entities/achivement/achivement.reducer';
import creditScore from 'app/entities/credit-score/credit-score.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  parents,
  child,
  balance,
  transactions,
  points,
  task,
  achivement,
  creditScore,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
