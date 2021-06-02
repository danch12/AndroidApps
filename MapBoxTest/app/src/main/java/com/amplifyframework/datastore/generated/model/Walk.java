package com.amplifyframework.datastore.generated.model;


import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Walk type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Walks")
public final class Walk implements Model {
  public static final QueryField ID = field("Walk", "id");
  public static final QueryField TITLE = field("Walk", "title");
  public static final QueryField DESCRIPTION = field("Walk", "description");
  public static final QueryField DURATION = field("Walk", "duration");
  public static final QueryField CREATOR = field("Walk", "creator");
  public static final QueryField START_LAT = field("Walk", "startLat");
  public static final QueryField START_LON = field("Walk", "startLon");
  public static final QueryField END_LON = field("Walk", "endLon");
  public static final QueryField END_LAT = field("Walk", "endLat");
  public static final QueryField SONG_IDS = field("Walk", "songIds");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String description;
  private final @ModelField(targetType="Int") Integer duration;
  private final @ModelField(targetType="String") String creator;
  private final @ModelField(targetType="Float") Double startLat;
  private final @ModelField(targetType="Float") Double startLon;
  private final @ModelField(targetType="Float") Double endLon;
  private final @ModelField(targetType="Float") Double endLat;
  private final @ModelField(targetType="String", isRequired = true) List<String> songIds;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getDescription() {
      return description;
  }
  
  public Integer getDuration() {
      return duration;
  }
  
  public String getCreator() {
      return creator;
  }
  
  public Double getStartLat() {
      return startLat;
  }
  
  public Double getStartLon() {
      return startLon;
  }
  
  public Double getEndLon() {
      return endLon;
  }
  
  public Double getEndLat() {
      return endLat;
  }
  
  public List<String> getSongIds() {
      return songIds;
  }
  
  private Walk(String id, String title, String description, Integer duration, String creator, Double startLat, Double startLon, Double endLon, Double endLat, List<String> songIds) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.duration = duration;
    this.creator = creator;
    this.startLat = startLat;
    this.startLon = startLon;
    this.endLon = endLon;
    this.endLat = endLat;
    this.songIds = songIds;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Walk walk = (Walk) obj;
      return ObjectsCompat.equals(getId(), walk.getId()) &&
              ObjectsCompat.equals(getTitle(), walk.getTitle()) &&
              ObjectsCompat.equals(getDescription(), walk.getDescription()) &&
              ObjectsCompat.equals(getDuration(), walk.getDuration()) &&
              ObjectsCompat.equals(getCreator(), walk.getCreator()) &&
              ObjectsCompat.equals(getStartLat(), walk.getStartLat()) &&
              ObjectsCompat.equals(getStartLon(), walk.getStartLon()) &&
              ObjectsCompat.equals(getEndLon(), walk.getEndLon()) &&
              ObjectsCompat.equals(getEndLat(), walk.getEndLat()) &&
              ObjectsCompat.equals(getSongIds(), walk.getSongIds());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getDescription())
      .append(getDuration())
      .append(getCreator())
      .append(getStartLat())
      .append(getStartLon())
      .append(getEndLon())
      .append(getEndLat())
      .append(getSongIds())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Walk {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("duration=" + String.valueOf(getDuration()) + ", ")
      .append("creator=" + String.valueOf(getCreator()) + ", ")
      .append("startLat=" + String.valueOf(getStartLat()) + ", ")
      .append("startLon=" + String.valueOf(getStartLon()) + ", ")
      .append("endLon=" + String.valueOf(getEndLon()) + ", ")
      .append("endLat=" + String.valueOf(getEndLat()) + ", ")
      .append("songIds=" + String.valueOf(getSongIds()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Walk justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Walk(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      description,
      duration,
      creator,
      startLat,
      startLon,
      endLon,
      endLat,
      songIds);
  }
  public interface TitleStep {
    SongIdsStep title(String title);
  }
  

  public interface SongIdsStep {
    BuildStep songIds(List<String> songIds);
  }
  

  public interface BuildStep {
    Walk build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep description(String description);
    BuildStep duration(Integer duration);
    BuildStep creator(String creator);
    BuildStep startLat(Double startLat);
    BuildStep startLon(Double startLon);
    BuildStep endLon(Double endLon);
    BuildStep endLat(Double endLat);
  }
  

  public static class Builder implements TitleStep, SongIdsStep, BuildStep {
    private String id;
    private String title;
    private List<String> songIds;
    private String description;
    private Integer duration;
    private String creator;
    private Double startLat;
    private Double startLon;
    private Double endLon;
    private Double endLat;
    @Override
     public Walk build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Walk(
          id,
          title,
          description,
          duration,
          creator,
          startLat,
          startLon,
          endLon,
          endLat,
          songIds);
    }
    
    @Override
     public SongIdsStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep songIds(List<String> songIds) {
        Objects.requireNonNull(songIds);
        this.songIds = songIds;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep duration(Integer duration) {
        this.duration = duration;
        return this;
    }
    
    @Override
     public BuildStep creator(String creator) {
        this.creator = creator;
        return this;
    }
    
    @Override
     public BuildStep startLat(Double startLat) {
        this.startLat = startLat;
        return this;
    }
    
    @Override
     public BuildStep startLon(Double startLon) {
        this.startLon = startLon;
        return this;
    }
    
    @Override
     public BuildStep endLon(Double endLon) {
        this.endLon = endLon;
        return this;
    }
    
    @Override
     public BuildStep endLat(Double endLat) {
        this.endLat = endLat;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String description, Integer duration, String creator, Double startLat, Double startLon, Double endLon, Double endLat, List<String> songIds) {
      super.id(id);
      super.title(title)
        .songIds(songIds)
        .description(description)
        .duration(duration)
        .creator(creator)
        .startLat(startLat)
        .startLon(startLon)
        .endLon(endLon)
        .endLat(endLat);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder songIds(List<String> songIds) {
      return (CopyOfBuilder) super.songIds(songIds);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder duration(Integer duration) {
      return (CopyOfBuilder) super.duration(duration);
    }
    
    @Override
     public CopyOfBuilder creator(String creator) {
      return (CopyOfBuilder) super.creator(creator);
    }
    
    @Override
     public CopyOfBuilder startLat(Double startLat) {
      return (CopyOfBuilder) super.startLat(startLat);
    }
    
    @Override
     public CopyOfBuilder startLon(Double startLon) {
      return (CopyOfBuilder) super.startLon(startLon);
    }
    
    @Override
     public CopyOfBuilder endLon(Double endLon) {
      return (CopyOfBuilder) super.endLon(endLon);
    }
    
    @Override
     public CopyOfBuilder endLat(Double endLat) {
      return (CopyOfBuilder) super.endLat(endLat);
    }
  }
  
}
