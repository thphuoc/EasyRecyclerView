package com.example.mda

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import java.lang.reflect.Field

class SuperclassExclusionStrategy : ExclusionStrategy {

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        val fieldName = f?.name
        val theClass = f?.declaringClass

        return isFieldInSuperclass(theClass, fieldName)
    }

    private fun isFieldInSuperclass(subclass: Class<*>?, fieldName: String?): Boolean {
        var superclass: Class<*>? = subclass?.superclass
        var field: Field?

        while (superclass != null) {
            field = getField(superclass, fieldName)

            if (field != null)
                return true

            superclass = superclass.superclass
        }

        return false
    }

    private fun getField(theClass: Class<*>, fieldName: String?): Field? {
        return try {
            theClass.getDeclaredField(fieldName)
        } catch (e: Exception) {
            null
        }

    }
}