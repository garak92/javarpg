package rpg.engine.quest;

import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class Quest {
  private String name;
  private String description;
  private String inProgressDialog;
  private String completedDialog;
  private Class<? extends BaseMonster> questEntityType = null;
  private Class<? extends BaseMonster> questGiver = null;
  private int entityCounter = 0;
  private int questObjective = 0;
  private int experiencePoints = 0;
  private EnumQuestStatus questStatus = EnumQuestStatus.AVAILABLE;
  private String entityFriendlyName;

  public <T extends BaseMonster> Quest(String name, String description, String inProgressDialog, String completedDialog,
      int experiencePoints, Class<? extends BaseMonster> questEntityType,
      int questObjective, Class<? extends BaseMonster> questGiver, String entityFriendlyName) {
    this.name = name;
    this.description = description;
    this.inProgressDialog = inProgressDialog;
    this.completedDialog = completedDialog;
    this.questEntityType = questEntityType;
    this.questObjective = questObjective;
    this.questGiver = questGiver;
    this.experiencePoints = experiencePoints;
    this.entityFriendlyName = entityFriendlyName;
  }

  public String getCurrentText() {
    switch (questStatus) {
      case AVAILABLE:
        return description;
      case IN_PROGRESS:
        return inProgressDialog;
      case COMPLETED:
        return completedDialog;
      default:
        break;
    }
    return description;
  }

  public void acceptQuest() {
    if (questStatus != EnumQuestStatus.AVAILABLE) {
      return;
    }
    questStatus = EnumQuestStatus.IN_PROGRESS;
  }

  public void deliverQuest(Player player) {
    if (questStatus != EnumQuestStatus.COMPLETED) {
      return;
    }
    questStatus = EnumQuestStatus.DELIVERED;
    player.addExperiencePoints(this.experiencePoints);
  }

  public <T extends BaseMonster> void update(T entity) {
    if (this.questStatus == EnumQuestStatus.COMPLETED) {
      return;
    }
    if (entity.getClass().equals(this.questEntityType)) {
      entityCounter++;
    }
    if (entityCounter == questObjective) {
      this.questStatus = EnumQuestStatus.COMPLETED;
      return;
    }
  }

  public boolean isInProgress() {
    return questStatus == EnumQuestStatus.IN_PROGRESS;
  }

  public String getName() {
    return name;
  }

  public EnumQuestStatus getQuestStatus() {
    return questStatus;
  }

  public int getEntityCounter() {
    return entityCounter;
  }

  public int getQuestObjective() {
    return questObjective;
  }

  public Class<? extends BaseMonster> getQuestGiver() {
    return questGiver;
  }

  public Class<? extends BaseMonster> getQuestEntityType() {
    return questEntityType;
  }

  public int getExperiencePoints() {
    return experiencePoints;
  }

  public String getEntityFriendlyName() {
    return entityFriendlyName;
  }

}
