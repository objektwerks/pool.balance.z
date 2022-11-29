package objektwerks

class Handler(store: Store):
  def handle(listPools: ListPools): PoolsListed = PoolsListed(store.listPools)
  def handle(savePool: SavePool): PoolSaved = PoolSaved(store.addPool(savePool.pool))
