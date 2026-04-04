package com.example.learning.entity;

import jakarta.persistence.Version;

public abstract class VersionedEntity extends Auditable {
  @Version
  private Integer version;
}
