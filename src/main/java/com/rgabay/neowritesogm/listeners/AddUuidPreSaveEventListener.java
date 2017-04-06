package com.rgabay.neowritesogm.listeners;

import com.rgabay.neowritesogm.domain.OgmTestNode;
import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListener;


public class AddUuidPreSaveEventListener implements EventListener {

   public void onPreSave(Event event) {
       System.out.println("on pre save");
      try{
          OgmTestNode entity = (OgmTestNode) event.getObject();
          if (entity.getId() == null) {
              entity.setCustomId(11);
          }
      } catch(java.lang.ClassCastException e){
          System.out.println("nothing to see here...");
      }

    }

   public void onPostSave(Event event) {
    }

  public void onPreDelete(Event event) {
    }

   public void onPostDelete(Event event) {
    }
}