package com.microstep.android.onclick.rest;

public class WebServiceFactory {
	
	private static NoteService noteService;
	
	public static NoteService getNoteService() {
		if(noteService == null){
			noteService = new NoteServiceImpl();
		}
		return noteService;
	}

}