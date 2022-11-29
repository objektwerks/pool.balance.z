package objektwerks

class Handler(store: Store): // Totally broken by 3 Quill type errors!
  def handle(listPools: ListPools): PoolsListed = PoolsListed(store.listPools)
  def handle(savePool: SavePool): PoolSaved =
    val pool = savePool.pool
    val id = if pool.id == 0 then store.addPool(pool)
             else store.updatePool(pool)
    PoolSaved(id)