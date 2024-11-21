package com.suslovila.kharium.api.process

object ProcessRegistry {
    private val nameToClass : HashMap<String, Class<IProcess>> = hashMapOf()

    fun registerProcess(classZ: Class<IProcess>, name: String) {
        if(nameToClass.containsKey(name)) throw  IllegalArgumentException("Duplicate id: $name")
        nameToClass[name] = classZ
    }
}