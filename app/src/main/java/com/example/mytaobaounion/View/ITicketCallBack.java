package com.example.mytaobaounion.View;

import com.example.mytaobaounion.Model.Domain.TicketModel;

public interface ITicketCallBack extends IBaseCallback{
    public void OnTicketCodeLoaded(String cover, TicketModel ticketModel);
}
