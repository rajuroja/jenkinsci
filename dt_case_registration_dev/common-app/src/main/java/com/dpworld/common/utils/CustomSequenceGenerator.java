package com.dpworld.common.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of the class is to implement Custom Sequence genarator for
 * Sequence No in all tables
 * 
 * @author Intech Creative Services Pvt. Ltd.
 *
 */

public class CustomSequenceGenerator extends SequenceStyleGenerator {

  private static Logger logger = LoggerFactory.getLogger(CustomSequenceGenerator.class);

  /**
   * This method is used to add generate details of Hibernate
   * 
   * @param tokenInfo {@link SharedSessionContractImplementor}
   * @return {@link Object}
   * 
   */
  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object) {

    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyMM");
      String monthYear = formatter.format(new Date());
      Long id = (Long) super.generate(session, object);
      return Long.parseLong(monthYear + String.valueOf(id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return 0;
  }
}
