module Enemy {
    requires Common;

    provides dk.sdu.cbse.common.services.IGamePluginService
        with dk.sdu.cbse.enemy.EnemyPlugin;

    provides dk.sdu.cbse.common.services.IEntityProcessingService
        with dk.sdu.cbse.enemy.EnemyControlSystem;
}
