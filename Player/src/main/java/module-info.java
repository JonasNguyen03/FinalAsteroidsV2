module Player {
    requires Common;
    requires spring.context;

    opens dk.sdu.cbse.player to spring.core, spring.beans, spring.context;
}