package edu.yale.its.tp.email.conversion.spring;

import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.*;

/**
 * <pre>
 * Copyright (c) 2000-2003 Yale University. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY
 * DISCLAIMED. IN NO EVENT SHALL YALE UNIVERSITY OR ITS EMPLOYEES BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED, THE COSTS OF
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED IN ADVANCE OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 * 
 * Redistribution and use of this software in source or binary forms,
 * with or without modification, are permitted, provided that the
 * following conditions are met:
 * 
 * 1. Any redistribution must include the above copyright notice and
 * disclaimer and this list of conditions in any related documentation
 * and, if feasible, in the redistributed software.
 * 
 * 2. Any redistribution must include the acknowledgment, "This product
 * includes software developed by Yale University," in any related
 * documentation and, if feasible, in the redistributed software.
 * 
 * 3. The names "Yale" and "Yale University" must not be used to endorse
 * or promote products derived from this software.
 * </pre>
 *
 */

public class MyPropertyOverrideConfigurer extends PropertyOverrideConfigurer {

	  /**
	  * Apply the given property value to the corresponding bean.
	  */
	  protected void applyPropertyValue(ConfigurableListableBeanFactory factory
			                          , String beanName
			                          , String property
			                          , String value) {
	  
		  /* Old way with our aliases */
//		  BeanDefinition bd = factory.getBeanDefinition(beanName);
		  
		  /* New Way */
		  BeanDefinition bd = getBeanDefFromAliases(factory, beanName);
		  if(bd == null) throw new NoSuchBeanDefinitionException(beanName);
		  bd.getPropertyValues().addPropertyValue(property, value);
	  }
	  
	  protected BeanDefinition getBeanDefFromAliases(ConfigurableListableBeanFactory factory
			                                       , String beanName){
		  BeanDefinition bd = null;
		  try{
			  bd = factory.getBeanDefinition(beanName);
		  } catch (NoSuchBeanDefinitionException nsbe){
			  String[] aliases = factory.getAliases(beanName);
			  for(String alias : aliases){
				  bd = getBeanDefFromAliases(factory, alias);
				  if(bd != null) return bd;
			  }
		  }
		  return bd;
	  }
}
