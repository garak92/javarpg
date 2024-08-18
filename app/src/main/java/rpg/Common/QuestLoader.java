package rpg.Common;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import rpg.Monsters.BaseMonster;
import rpg.Monsters.Quest;

import rpg.App;

public class QuestLoader {

  public static void loadQuests() {
    try (InputStream questsFile = App.class.getClassLoader().getResourceAsStream("quests/Quests.xml");) {
      DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document xmldoc = docBuilder.parse(questsFile);

      NodeList questElementList = xmldoc.getElementsByTagName("Quest");
      System.out.println(questElementList.getLength());
      for (int i = 0; i < questElementList.getLength(); i++) {
        Element elem = (Element) questElementList.item(i);
        System.out.println(elem.getElementsByTagName("Entity").item(0).getTextContent().trim());
        Class<? extends BaseMonster> questEntityType = Class
            .forName(elem.getElementsByTagName("Entity").item(0).getTextContent().trim()).asSubclass(BaseMonster.class);
        Class<? extends BaseMonster> questGiver = Class
            .forName(elem.getElementsByTagName("Giver").item(0).getTextContent().trim()).asSubclass(BaseMonster.class);

        Quest quest = new Quest(
            elem.getElementsByTagName("Name").item(0).getTextContent().trim(),
            elem.getElementsByTagName("Description").item(0).getTextContent().trim(),
            elem.getElementsByTagName("DialogInProgress").item(0).getTextContent().trim(),
            elem.getElementsByTagName("DialogCompleted").item(0).getTextContent().trim(),
            Integer.valueOf(elem.getElementsByTagName("ExperiencePoints").item(0).getTextContent().trim()),
            questEntityType,
            Integer.valueOf(elem.getElementsByTagName("ObjectiveQuantity").item(0).getTextContent().trim()),
            questGiver, elem.getElementsByTagName("EntityFriendlyName").item(0).getTextContent().trim());

        QuestLog.INSTANCE.addQuest(quest);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
