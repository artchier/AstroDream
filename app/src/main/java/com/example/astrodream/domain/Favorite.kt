package com.example.astrodream.domain

// TODO transformar type em um enum (FavoriteType)
// type         descrip1                 descrip2
//-----------------------------------------------------------------
// "today"      Descrição da imagem     Dia
// "asteroid"   Nome                    Dia mais proximo da Terra
// "globe"      Dia                     ---
// "tech"       Tipo                    Titulo
// "mars"       Dia em Sol              Dia
class Favorite (val type: String, var descrip1: String, var descrip2: String, var img: String)