package com.example.astrodream.domain

// TODO transformar type em um enum (FavoriteType)
// type         id                          data1               data2                       data3
//-----------------------------------------------------------------------------------------------------------
// "daily"      id do item na tabela db     Titulo da imagem    Dia                         url da imagem
// "asteroid"   id do item na tabela db     Nome                Dia mais proximo da Terra   url da imagem
// "globe"      id do item na tabela db     Dia                 ---                         url da imagem
// "tech"       id do item na tabela db     Tipo                Titulo                      url da imagem
// "mars"       id do item na tabela db     Dia em Sol          Dia                         url da imagem
class Favorite (val type: String, val id: Int, var data1: String, var data2: String, var data3: String)