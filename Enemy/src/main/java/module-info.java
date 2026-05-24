module Enemy {
    requires Common;
    requires spring.context;

    opens dk.sdu.cbse.enemy to spring.core, spring.beans, spring.context;
}