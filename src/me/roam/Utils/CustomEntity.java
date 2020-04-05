package me.roam.Utils;

import java.lang.reflect.Field;
import java.util.Map;

import net.minecraft.server.v1_8_R3.Entity;

public enum CustomEntity {

    FREECAM("Zombie", 54, Freecam.class);

    private CustomEntity(String name, int id, Class<? extends Entity> custom) {
        addToMaps(custom, name, id);
    }

   @SuppressWarnings("unchecked")
   public static void addToMaps(Class clazz, String name, int id) {
        ((Map)getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
        ((Map)getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        ((Map)getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
    }
   
   public static Object getPrivateField(String fieldName, Class clazz, Object object) {
       Field field;
       Object o = null;
       try
       {
           field = clazz.getDeclaredField(fieldName);
           field.setAccessible(true);
           o = field.get(object);
       }
       catch(NoSuchFieldException e)
       {
           e.printStackTrace();
       }
       catch(IllegalAccessException e)
       {
           e.printStackTrace();
       }
       return o;
   }
}