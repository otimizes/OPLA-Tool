package br.ufpr.dinf.gres.architecture.builders;

import br.ufpr.dinf.gres.architecture.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.architecture.io.OPLAConfigThreadScope;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Variability;
import br.ufpr.dinf.gres.architecture.representation.Variant;
import br.ufpr.dinf.gres.architecture.util.Constants;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class ArchitectureBuilderSMartyTest {

    @Test
    public void buildAGM1onSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        String xmiFilePath = agm + Constants.FILE_SEPARATOR + "agm1.smty";
        Architecture architecture = new ArchitectureBuilderSMarty().create(xmiFilePath);

        assertEquals(9, architecture.getAllPackages().size());
        assertEquals(29, architecture.getAllClasses().size());
        assertEquals(14, architecture.getAllInterfaces().size());
        assertEquals(4, architecture.getAllVariabilities().size());
        assertEquals(12, architecture.getAllVariants().size());
        assertEquals(4, architecture.getAllVariationPoints().size());
        assertEquals(40, architecture.getAllAtributtes().size());
        assertEquals(168, architecture.getAllMethods().size());

        String methods = "overlaps,paint,collidewith,point,collidewith,paint,absorb,incscore,resetscore,savescore,exitgame,viewtopscores,savegame,getgame,loadgame,startmoving,paint,moving,setdirection,collidewith,stopmoving,move,viewtopscores,savegame,loadgame,savescore,initialize,getsavedata,getboundingbox,collidewith,setsavedata,paintbricks,paint,install,save,exit,gamegui,gameboardmgr,setpartialscore,getpartialscore,endmatch,setfinalscore,getfinalscore,startmatch,display,loadgame,savegame,viewtopscores,savescore,getsavedata,setdirection,collidewith,move,delete,paint,setsavedata,isheadingup,isheadingright,isheadingdown,isreadingleft,checkforcollision,startmovement,getscore,removemovablepiece,handletick,stopmovement,paint,addmovablepiece,removestationarypiece,run,setmenu,alreadycollided,setscore,buildgameboard,getgameover,initializecollisionlist,getmenu,setspeed,addstationarypiece,resetlist,exitgame,getspeed,tick,paint,collidewith,absorb,gameboardctrl,play,playgamegui,savegame,savescore,viewtopscores,loadgame,gamectrl,gamemgr,collidewith,paint,setsavedata,moveleft,move,paint,moveright,collidewith,getsavedata,hitbypuck,getsavedata,paint,collidewith,setsavedata,die,gameboardgui,checkscore,savescore,startanimation,stopanimation,hasscore,showscore,getgame,checkinstallation,exit,addgame,uninstall,remgame,play,savegame,install,getgame,loadgame,checkinstallation,play,exit,checkgameboard,setpartialscore,remsprite,getsprite,setfinalscore,getfinalscore,endmatch,setgameboard,getlocation,startgameboard,startmatch,savescore,showscore,addsprite,getpartialscore,getgameboard,checkgame,uninstall,getgameboard,stopanimation,startanimation,savegame,checkmatch,checkgameboard,play,checkinstallation,checkgame,install,play,checkinstallation,initialize,isarankingscore,saveinginranking,getscore,isgameplayed,setdateranking,savescore";
        assertEquals(methods, architecture.getAllMethods().stream().map(Element::getName).collect(Collectors.joining(",")));
        String attrs = "icon,localization,name,y,x,score,gamecode,lastPosition,currentvelocity,ismoving,numberofsets,gamecode,setpoints,numberofbricks,numleft,numrows,numofbricksperrow,pile,numberofmatches,numberofballs,gamecode,isdead,puckdimension,speed,direction,dx,dy,display,moving,speed,menu,image,movablecomponents,score,stationarycomponents,gameover,collisionlist,numberoftruns,gamecode,isbroken";
        assertEquals(attrs, architecture.getAllAtributtes().stream().map(Element::getName).collect(Collectors.joining(",")));
        String classes = "Sprite,Point,Floor,Score,Game,MovableSprites,PongGame,InitializationMgr,BrickPile,GameGUI,GameBoardMgr,Match,Display,StationarySprite,BricklesGame,Puck,Velocity,GameBoard,Wall,GameBoardCtrl,PlayGameGUI,BowlingGame,GameCtrl,GameMgr,Ceiling,Paddle,Brick,GameBoardGUI,AnimationLoopMgr";
        assertEquals(classes, architecture.getAllClasses().stream().map(Element::getName).collect(Collectors.joining(",")));
        String interfaces = "ICheckScore,IGameMgt,IPlayBrickles,IExitGame,IGameBoardMgt,IUninstallGame,IGameBoardData,IAnimationLoopMgt,ISaveGame,IPlayPong,IInstallGame,IPlayBowling,IInitializationMgt,ISaveScore";
        assertEquals(interfaces, architecture.getAllInterfaces().stream().map(Element::getName).collect(Collectors.joining(",")));
        String variationPoints = "Game,MovableSprites,Sprite,StationarySprite";
        assertEquals(variationPoints, architecture.getAllVariationPoints().stream().map(variationPoint -> variationPoint.getVariationPointElement().getName()).collect(Collectors.joining(",")));
        String variabilities = "game,movable_sprit,sprit,stationary_sprit";
        assertEquals(variabilities, architecture.getAllVariabilities().stream().map(Variability::getName).collect(Collectors.joining(",")));
        String variants = "BowlingGame,PongGame,BricklesGame,Puck,Paddle,MovableSprites,StationarySprite,Ceiling,Floor,Wall,Brick,BrickPile";
        assertEquals(variants, architecture.getAllVariants().stream().map(Variant::getName).collect(Collectors.joining(",")));
    }

    @Test
    public void buildAGM2onSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        String xmiFilePath = agm + Constants.FILE_SEPARATOR + "agm2.smty";
        Architecture architecture = new ArchitectureBuilderSMarty().create(xmiFilePath);

        assertEquals(9, architecture.getAllPackages().size());
        assertEquals(30, architecture.getAllClasses().size());
        assertEquals(14, architecture.getAllInterfaces().size());
        assertEquals(4, architecture.getAllVariabilities().size());
        assertEquals(12, architecture.getAllVariants().size());
        assertEquals(4, architecture.getAllVariationPoints().size());
        assertEquals(58, architecture.getAllAtributtes().size());
        assertEquals(179, architecture.getAllMethods().size());

        String methods = "overlaps,paint,collidewith,point,collidewith,paint,absorb,incscore,updatescoreranking,resetscore,savescore,exitgame,viewtopscores,savegame,getgame,loadgame,startmoving,paint,moving,setdirection,collidewith,stopmoving,move,viewtopscores,savegame,loadgame,savescore,initialize,checkhigherscore,updatelasttimesaved,checklogin,updatelasttimeplayed,updatedatescore,checkpassword,getsavedata,getboundingbox,collidewith,setsavedata,paintbricks,paint,install,save,exit,gamegui,gameboardmgr,setpartialscore,getpartialscore,endmatch,setfinalscore,getfinalscore,startmatch,display,loadgame,savegame,viewtopscores,savescore,getsavedata,setdirection,collidewith,move,delete,paint,setsavedata,isheadingup,isheadingright,isheadingdown,isreadingleft,checkforcollision,startmovement,getscore,removemovablepiece,handletick,stopmovement,paint,addmovablepiece,removestationarypiece,run,setmenu,alreadycollided,setscore,buildgameboard,getgameover,initializecollisionlist,getmenu,setspeed,addstationarypiece,resetlist,exitgame,getspeed,tick,paint,collidewith,absorb,gameboardctrl,play,playgamegui,savegame,savescore,viewtopscores,loadgame,gamectrl,gamemgr,collidewith,paint,setsavedata,moveleft,move,paint,moveright,collidewith,getsavedata,hitbypuck,getsavedata,paint,collidewith,setsavedata,die,gameboardgui,checkscore,savescore,startanimation,stopanimation,hasscore,showscore,getgame,showrankingscores,checkinstallation,exit,addgame,uninstall,remgame,play,savegame,install,getgame,loadgame,checkinstallation,setdateplayed,play,exit,checkgameboard,setpartialscore,remsprite,getsprite,setfinalscore,getfinalscore,endmatch,setgameboard,getlocation,startgameboard,startmatch,savescore,showscore,addsprite,getpartialscore,getgameboard,checkgame,uninstall,getgameboard,stopanimation,startanimation,savegame,checkmatch,checkgameboard,setdateplayed,play,checkinstallation,checkgame,install,setdateplayed,play,checkinstallation,initialize,isarankingscore,saveinginranking,getscore,isgameplayed,setdateranking,savescore";
        assertEquals(methods, architecture.getAllMethods().stream().map(Element::getName).collect(Collectors.joining(",")));
        String attrs = "icon,localization,name,y,x,scorerankingbrickles,scorerankingpong,score,scorerankingbowling,gamecode,lastPosition,currentvelocity,ismoving,numberofsets,gamecode,setpoints,datebowlingscore,login,lasttimesavedbrickles,name,lasttimesavebowling,password,lasttimeplayedbowling,higherscorebrickles,datebricklesscore,datepongscore,higherscorepong,lasttimesavedpong,lasttimeplayedpong,higherscorebowling,lasttimeplayedbrickles,numberofbricks,numleft,numrows,numofbricksperrow,pile,numberofmatches,numberofballs,gamecode,isdead,puckdimension,speed,direction,dx,dy,display,moving,speed,menu,image,movablecomponents,score,stationarycomponents,gameover,collisionlist,numberoftruns,gamecode,isbroken";
        assertEquals(attrs, architecture.getAllAtributtes().stream().map(Element::getName).collect(Collectors.joining(",")));
        String classes = "Sprite,Point,Floor,Score,Game,MovableSprites,PongGame,InitializationMgr,Player,BrickPile,GameGUI,GameBoardMgr,Match,Display,StationarySprite,BricklesGame,Puck,Velocity,GameBoard,Wall,GameBoardCtrl,PlayGameGUI,BowlingGame,GameCtrl,GameMgr,Ceiling,Paddle,Brick,GameBoardGUI,AnimationLoopMgr";
        assertEquals(classes, architecture.getAllClasses().stream().map(Element::getName).collect(Collectors.joining(",")));
        String interfaces = "ICheckScore,IGameMgt,IPlayBrickles,IExitGame,IGameBoardMgt,IUninstallGame,IGameBoardData,IAnimationLoopMgt,ISaveGame,IPlayPong,IInstallGame,IPlayBowling,IInitializationMgt,ISaveScore";
        assertEquals(interfaces, architecture.getAllInterfaces().stream().map(Element::getName).collect(Collectors.joining(",")));
        String variationPoints = "Game,MovableSprites,Sprite,StationarySprite";
        assertEquals(variationPoints, architecture.getAllVariationPoints().stream().map(variationPoint -> variationPoint.getVariationPointElement().getName()).collect(Collectors.joining(",")));
        String variabilities = "game,movable_sprit,sprit,stationary_sprit";
        assertEquals(variabilities, architecture.getAllVariabilities().stream().map(Variability::getName).collect(Collectors.joining(",")));
        String variants = "BowlingGame,PongGame,BricklesGame,Puck,Paddle,MovableSprites,StationarySprite,Ceiling,Floor,Wall,Brick,BrickPile";
        assertEquals(variants, architecture.getAllVariants().stream().map(Variant::getName).collect(Collectors.joining(",")));
    }

    @Test
    public void buildAGMAtualonSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        String xmiFilePath = agm + Constants.FILE_SEPARATOR + "AGMAtual.smty";
        Architecture architecture = new ArchitectureBuilderSMarty().create(xmiFilePath);

        assertEquals(9, architecture.getAllPackages().size());
        assertEquals(30, architecture.getAllClasses().size());
        assertEquals(14, architecture.getAllInterfaces().size());
        assertEquals(4, architecture.getAllVariabilities().size());
        assertEquals(12, architecture.getAllVariants().size());
        assertEquals(4, architecture.getAllVariationPoints().size());
        assertEquals(62, architecture.getAllAtributtes().size());
        assertEquals(169, architecture.getAllMethods().size());

        String methods = "startAnimation,stopAnimation,collideWidth,paint,absorb,updateScoreRanking,incScore,resetScore,loadGame,exitGame,getGame,saveScore,saveGame,viewTopScores,setDirection,paint,moving,stopMoving,startMoving,move,collideWith,loadGame,saveScore,saveGame,viewTopScores,paint,overLaps,collideWith,checkPassword,updateLastTimeSaved,updateDateScore,setHeigherScore,updateLastTimePlayed,checkLogin,setSaveData,getSaveData,collideWith,paint,getBoundingBox,paintBricks,install,save,exit,setPartialScore,endMatch,setFinalScore,getFinalScore,startMatch,getPartialScore,saveScore,loadGame,saveGame,viewTopScores,collideWith,move,getSaveData,delete,setDirection,paint,setSaveData,isHeadingLeft,isHeadingDown,isHeadingUp,isHeadingRight,resetList,alreadyCollision,exitGame,addStationaryPiece,getGameOver,handleTick,getScore,removeMovablePiece,StartMovement,paint,buildGameBoard,run,setScore,getSpeed,initializeCollisionList,removeStationaryPiece,addMovabalePiece,checkForCollision,setSpeed,stopMovement,setMenu,getMenu,tick,paint,collideWith,absorb,play,exit,config,viewTopScores,loadGame,saveGame,saveScore,collideWith,paint,setSaveData,moveRight,paint,collideWith,moveLeft,getSaveData,initialize,hitbyPuck,paint,collideWith,getSaveData,setSaveData,die,checkScore,saveScore,showScore,getGame,hasScore,showRankingScores,checkInstallation,exit,saveGame,getGame,uninstall,play,loadGame,install,addGame,remGame,uninstall,checkGame,checkinstallation,setDatePlayed,play,checkGameBoard,exit,getPartialScore,showScore,startGameBoard,setGameBoard,getSprite,endMatch,remSprite,setPartialScore,addSprite,getFinalScore,getLocation,saveScore,setFinalScore,getGameBoard,stopAnimation,startAnimation,checkGameBoard,saveGame,checkMatch,play,setDatePlayed,checkInstallation,checkGame,install,play,setDatePlayed,checkInstallation,initialize,saveScore,setDateRanking,saveingInRanking,isGamePlayed,getScore,isARankingScore";
        assertEquals(methods, architecture.getAllMethods().stream().map(Element::getName).collect(Collectors.joining(",")));
        String attrs = "scoreRankingPong,score,scoreRankingBrickles,scoreRankingBowling,gameCode,currentVelocity,lastPosition,isMoving,setPoints,gameCode,numberOfSets,icon,localization,name,datePongScore,login,lastTimeSavedPong,lastTimePlayedBrickles,lastTimePlayedPong,lastTimeSavedBrickles,heigherScoreBrickles,name,password,heigherScoreBowling,lastTimePlayedBowling,dateBowlingScore,heigherScorePong,numRows,numOfBricksPerRow,numberOfBricks,Brick,numLeft,pile,att8,attr5,numberOfMatches,numbersOfBalls,gameCode,isDead,puckDimension,speed,direction,dx,dy,display,stationaryComponents,movableComponents,moving,speed,menu,image,score,collisionList,gameOver,attr10,attr6,numberOfTruns,gameCode,nameAttr1,attr7,isBroken,Attr4";
        assertEquals(attrs, architecture.getAllAtributtes().stream().map(Element::getName).collect(Collectors.joining(",")));
        String classes = "Point,AnimationLoppMgr,Floor,Score,Game,MovableSprites,PongGame,Sprit,Player,BrickPile,GameGUI,GameBoardMgr,Match,Display,StationarySprite,BricklesGame,Puck,Velocity,GameBoard,Wall,GameBoardCtrl,PlayGameGUI,BowlingGame,GameCtrl,GameMgr,Ceiling,Paddle,InitializeMgr,Brick,GameBoardGUI";
        assertEquals(classes, architecture.getAllClasses().stream().map(Element::getName).collect(Collectors.joining(",")));
        String interfaces = "ICheckScore,IGameMgt,IUninstallgame,IPlayBrickles,IExitGame,IGameBoardMgt,IGameBoardData,IAnimationLoopMgt,ISaveGame,IPlayPong,IInstallGame,IPlayBowling,InitializationMgt,ISaveScore";
        assertEquals(interfaces, architecture.getAllInterfaces().stream().map(Element::getName).collect(Collectors.joining(",")));
        String variationPoints = "Game,StationarySprite,Sprit,MovableSprites";
        assertEquals(variationPoints, architecture.getAllVariationPoints().stream().map(variationPoint -> variationPoint.getVariationPointElement().getName()).collect(Collectors.joining(",")));
        String variabilities = "game,stationay_sprit,sprit,movable_sprite";
        assertEquals(variabilities, architecture.getAllVariabilities().stream().map(Variability::getName).collect(Collectors.joining(",")));
        String variants = "PongGame,BricklesGame,BowlingGame,Ceiling,Floor,Wall,Brick,BrickPile,StationarySprite,MovableSprites,Puck,Paddle";
        assertEquals(variants, architecture.getAllVariants().stream().map(Variant::getName).collect(Collectors.joining(",")));
    }

    @Test
    public void buildBetAtualonSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        String xmiFilePath = agm + Constants.FILE_SEPARATOR + "BetAtual.smty";
        Architecture architecture = new ArchitectureBuilderSMarty().create(xmiFilePath);

        assertEquals(56, architecture.getAllPackages().size());
        assertEquals(115, architecture.getAllClasses().size());
        assertEquals(30, architecture.getAllInterfaces().size());
        assertEquals(6, architecture.getAllVariabilities().size());
        assertEquals(8, architecture.getAllVariants().size());
        assertEquals(6, architecture.getAllVariationPoints().size());
        assertEquals(156, architecture.getAllAtributtes().size());
        assertEquals(571, architecture.getAllMethods().size());

        String methods = "criarSistViarioUrbano,alterarSistViarioUrbano,montarSistViarioUrbano,buscarSistViarioUrbano,dispararNovaMensagem,limpar,mostrar,criarCargo,buscarCargo,alterarCargo,montarCargo,buscarCargos,buscarCartoes,buscarCartoesPorPassageiro,buscarCartao,buscarPassageiro,buscarCartaoPorID,alterarViagem,criarViagem,buscarViagens,montarViagem,buscarViagem,validarIntegracao,verificarIntegracao,processarIntegracao,criarEmpresa,atribuirEmpresaAoPassageiro,buscarEmpresaUsuariaPorCNPJ,buscarPassageirosPorEmpresa,buscarEmpUsu,buscarEmpresasUsuarias,buscarEmpresaUsuaria,buscarEmpresaPorPassageiro,removerEmpresa,buscarCartaoPorID,buscarPassageiro,buscarCartoes,buscarCartao,buscarCartoesPorPassageiro,autenticarUsuario,buscarPassageiros,buscarPassageiro,atribuirEmpresaAoPassageiro,alterarPassageiro,criarPassageiro,removerValidador,adicionarValidador,buscarCartao,alterarCartao,podeCriarCartao,buscarCartoes,criarCartao,processarViagem,processarIntegracao,verificarIntegracao,registrarResposta,iniciarValidador,tratarCartao,permitirLeitura,buscarCartao,buscarCartoes,pagamentoCartao,verificarTipoIncompativeis,validarCombinacao,processarIntegracaoCombinacao,processarViagem,verificarIntegracao,buscarCartoes,buscarCartaoPorID,aquisicaoCartaoCombinacao,buscarCartao,buscarCartoesPorPassageiro,buscarTiposPermitidos,criarPassageiro,solicitarCartao,montarPassageiro,alterarCartao,buscarTipoPassageiro,buscarUltimaViagem,podeDebitar,buscarViagens,buscarViagensPorCartao,buscarCartao,buscarTiposPermitidos,carregarCartao,registrarViagem,debitarPassagem,buscarCartoes,criarCartao,buscarTipoPassageiroPorCartao,buscarPagamento,validarCartao,buscarSaldo,removerCartao,buscarTodosOnibus,verificarPermissaoViagem,criarLinha,criarOnibus,buscarCorrida,removerLinha,removerValidador,buscarValidador,removerCorrida,registrarArrecadacao,removerOnibus,buscarLinhas,criarValidador,buscarValidadoresNaoEmUso,alterarOnibus,alterarCorrida,alterarValidador,atualizarCorrida,buscarValidadores,alterarLinha,buscarLinha,buscarCorridas,registrarCredito,verificarIntegracao,verificarIntegracao,buscarPassageiro,buscarCartoesPorPassageiro,buscarCartao,buscarEmpresaUsuaria,buscarCartoes,buscarCartaoPorID,criarTipoPassageiro,buscarTiposPermitidos,buscarTipoPassageiro,salvarTipoPassageiro,alterarTipoPassageiro,buscarTiposPassageiros,buscarTodosTipos,buscarEmpresaPorPassageiro,buscarEmpresas,buscarEmpresaUsuariaPorCNPJ,calcularValorPassagem,processarViagem,processarViagem,calcularValorPassagem,montarPassageiro,buscarPassageiros,criarPassageiro,buscarPassageiro,alterarPassageiro,buscarTipoPassageiroLimPassagens,buscarLimPassagensPorTipo,buscarTodosTipos,criarTerminal,alterarTerminal,removerTerminal,buscarTerminal,atribuirValidadorAoTerminal,buscarTodosCartoes,buscarQuantidadePassagensPorCartao,solicitarCarga,buscarCartao,buscarCartoes,buscarLinhaIntegrada,alterarLinhaIntegrada,criarLinhaIntegrada,buscarLinhasIntegracao,buscarPagamentos,criarPagamento,montarPagamento,buscarPagamento,alterarPagamento,criarCargo,buscarCargos,montarCargo,alterarCargo,buscarCargo,alterarMaxNumViagens,buscarMaxNumViagens,salvarNumViagens,buscarSistUrbanoNumViagens,criarMaxNumViagens,buscarCartoes,alterarCartao,buscarCartao,montarCartao,criarCartao,buscarTodosTipos,verificarPossibilidadeCarga,salvarCartaoLimPassagens,buscarCartaoLimPassagens,salvarTipoPassageiroLimPassagens,buscarTipoPassageiroLimPassagens,buscarQuantidadePassagens,buscarLimPassagensPorTipo,buscarTodosCartoes,buscarQuantidadePassagensPorCartao,registrarTipoPassageiroLimPassagens,processarViagem,processarIntegracao,verificarIntegracao,buscarTodosTipos,buscarPagamentosCartao,criarPagamentoCartao,buscarTiposPagamentosPermitidos,alterarPagamentoCartao,buscarTipoPassageiroPagamentoCartao,buscarUltimoPagamento,registrarTipoPassageiroPagamentoCartao,buscarTipoPagamentoPorTipoPassageiro,buscarPagamentoPagamentoCartao,buscarUltimoTipoPassageiro,buscarCartoes,buscarCartao,criarCartao,alterarSistViarioUrbano,montarSistViarioUrbano,buscarSistViarioUrbano,criarSistViarioUrbano,removerPassageiro,adicionarPassageiros,obterTempo,montarFuncionario,buscarFuncionarios,criarFuncionario,buscarFuncionario,alterarFuncionario,montarTerminal,buscarTerminal,alterarTerminal,associarValidadorAoTerminal,criarTerminal,alterarValidador,buscarValidador,buscarValidadores,criarValidador,montarValidador,buscarEmpresaUsuaria,alterarEmpresaUsuaria,criarEmpresaUsuaria,buscarPagamentosCartao,buscarUltimoPagamento,acessoAdicional,girarCatraca,liberarCatraca,travarCatraca,dispararCatracaLiberada,alterarLinhaIntegrada,alterarLinhaIntegracao,removerLinhaIntegrada,salvarLinhaIntegrada,buscarLinhaIntegrada,verificarLinhaIntegrada,criarLinhaIntegrada,buscarLinhasIntegradas,buscarLinhasIntegracao,buscarIntegracao,passarCartao,alterarOnibus,buscarOnibus,buscarTodosOnibus,montarOnibus,criarOnibus,buscarTipoPassageiro,alterarTipoPassageiro,criarTipoPassageiro,buscarTiposPassageiros,montarTipoPassageiroLimPassagens,buscarSistViarioUrbano,alterarSistViarioUrbano,montarSistViarioUrbano,criarSistViarioUrbano,buscarSistViarioUrbano,alterarSistViarioUrbano,montarSistViarioUrbano,criarSistViarioUrbano,criarPagamento,buscarCartao,buscarPagamentos,buscarCartoes,buscarPagamento,alterarTipoPassageiro,buscarTipoPassageiroPorCartao,alterarPagamento,podeDebitar,removerPagamento,removerCartao,validarCartao,criarCartao,removerViagem,criarPagamentoPagamentoCartao,buscarTiposPermitidos,alterarViagem,criarViagem,buscarViagensPorCartao,carregarCartao,buscarUltimaViagem,buscarTipoPassageiro,alterarCartao,buscarTiposPassageiros,criarTipoPassageiro,buscarSaldo,removerTipoPassageiro,buscarViagens,buscarViagem,processarViagem,calcularValorPassagem,buscarCartao,alterarCartao,criarCartao,buscarCartoes,buscarEmpresaViaria,montarEmpresaViaria,alterarEmpresaViaria,criarEmpresaViaria,alterarLinha,criarLinha,buscarLinha,buscarLinhas,montarLinha,login,adicionaMenu,obterNivelAcesso,acessarAutorizacao,buscarCorrida,alterarCorridas,montarCorridas,buscarCorridas,criarCorridas,obterNumViagens,buscarCartoes,buscarPassageiro,buscarCartao,buscarCartoesPorPassageiro,buscarCartaoPorID,processaIntegracao,processarViagem,verificarIntegracao,buscarSistViarioUrbano,buscarTarifas,alterarSistViarioUrbano,removerTarifa,alterarTarifa,buscarEmpresaViaria,criarEmpresaViaria,buscarTarifa,criarSistViarioUrbano,criarTarifa,alterarEmpresaViaria,registrarCorrida,verificarIntegracao,buscarTipoPagamentoPorTipoPassageiro,buscarUltimoTipoPassageiro,processarIntegracao,processarViagem,verificarIntegracao,processaIntegracao,verificarIntegracao,processarViagem,buscarSistViarioUrbanoTempo,salvarTempo,alterarTempo,criarTempo,buscarTempo,buscarTiposPassageiros,buscarTipoPassageiro,montarTipoPassageiro,criarTipoPassageiro,alterarTipoPassageiro,buscarCargos,alterarCargo,removerFuncionario,criarFuncionario,removerCargo,alterarFuncionario,buscarFuncionarios,criarCargo,buscarCargo,buscarFuncionario,alterarTipoPassageiro,buscarTipoPassageiro,montarTipoPassageiroPagamentoCartao,criarTipoPassageiro,montarTipoPassageiro,buscarTiposPassageiros,montarPagamento,buscarPagamentos,criarPagamento,buscarPagamento,alterarPagamento,processarTransacao,solicitarGerencia,processarViagem,processarIntegracao,verificarIntegracao,calcularValorPassagem,processarViagem,criarSistViarioUrbano,alterarSistViarioUrbano,buscarSistViarioUrbano,salvarNumeroCartoes,buscarLimiteCartoes,buscarSistViarioUrbanoNumCartoes,criarLimiteCartoes,lterarLimiteCartoes,buscarCartao,buscarCartoes,solicitarCarga,verificarIntegracao,processarIntegracao,processarViagem,montarTipoPassageiro,bsucarCartao,buscarCartoes,solicitarCarga,removerTarifa,criarEmpresaViaria,alterarSistViarioUrbano,alterarEmpresaViaria,buscarSistViarioUrbano,buscarEmpresaViaria,alterarTarifa,criarTarifa,buscarTarifas,buscarTarifa,criarSistViarioUrbano,buscarTiposPermitidos,solicitarCartao,getValidadorID,setValidadorID,setOnibusID,getOnibusID,criarTerminal,atribuirValidadorAoTerminal,buscarTerminal,alterarTerminal,removerTerminal,obterTempo,buscarLimPassagensPorTipo,salvarCartaoLimPassagens,buscarQuantidadePassagens,registrarTipoPassageiroLimPassagens,buscarQuantidadePassagensPorCartao,buscarTodosCartoes,salvarTipoPassageiroLimPassagens,verificarPossibilidadeCarga,buscarTodosTipos,buscarTipoPassageiroLimPassagens,buscarCartaoLimPassagens,buscarPagamento,alterarTipoPassageiro,buscarTipoPassageiroPorCartao,validarCartao,removerCartao,criarCartao,buscarCartao,buscarCartoes,buscarPagamentos,podeDebitar,alterarPagamento,removerPagamento,criarViagem,criarPagamento,removerViagem,buscarViagensPorCartao,removerTipoPassageiro,buscarTiposPermitidos,carregarCartao,buscarViagens,alterarViagem,buscarViagem,buscarTipoPassageiro,buscarSaldo,criarTipoPassageiro,alterarCartao,buscarUltimaViagem,buscarTiposPassageiros,registrarViagem,validarCartao,verificarFormaPagamento,debitarPasagem,obterValorPassagem,registrarArrecadacao,registrarCredito,verificarTiposIncompativeis,validarCombinacao,tratarCartao,buscarOnibus,alterarLinha,buscarLinhaAtualValidador,buscarLinhas,removerCorrida,buscarLinha,criarOnibus,criarCorrida,buscarValidador,criarLinha,verificarPermissaoViagem,criarValidador,buscarTodosOnibus,alterarCorrida,removerLinha,removerValidador,alterarValidador,buscarCorridas,removerOnibus,buscarValidadoresNaoEmUso,buscarCorrida,buscarValidadores,alterarOnibus,buscarMaxNumViagens,criarMaxNumViagens,alterarMaxNumViagens,permitirLeitura,removerLinhaIntegrada,buscarLinhasIntegracao,criarLinhaIntegrada,verificarLinhaIntegrada,alterarLinhaIntegrada,buscarLinhaIntegrada,buscarLinhasIntegradas,buscarIntegracao,obterValorPassagem,obterValorCartao,removerCargo,buscarFuncionarios,alterarFuncionario,buscarCargos,buscarFuncionario,criarFuncionario,criarCargo,alterarCargo,removerFuncionario,buscarCargo,solicitarGerencia,atualizarTempo,buscarTempo,criarTempo,processarTransacao,buscarLimiteCartoes,criarLimiteCartoes,alterarLimiteCartoes,buscarPagamentosCartao,buscarTipoPagamentoPorTipoPassageiro,buscarUltimoTipoPassageiro,buscarTodosTipos,registrarTipoPassageiroPagamentoCartao,buscarTiposPagamentosPermitidos,alterarPagamentoCartao,criarPagamentoCartao,buscarPagamentoCartao,buscarTipoPassageiroPagamentoCartao,buscarUltimoPagamento,atualizarCorrida,solicitarCarga,processarViagem,obterNumViagens,registrarCorrida,alterarEmpresa,criarEmpresa,buscarEmpresaUsuaria,buscarPassageirosPorEmpresa,buscarEmpresasUsuarias,buscarEmpUsu,buscarEmpresaUsuariaPorCNPJ,atribuirEmpresaAoPassageiro,buscarEmpresaPorPassageiro,removerEmpresa,existePassageiro,buscarQuantidadeCartoesPorPassageiro,alterarPassageiro,buscarCartoesPorPassageiro,removerPassageiro,criarPassageiro,removerPassageiroPorID,buscarPassageiro,buscarPassageiros";
        assertEquals(methods, architecture.getAllMethods().stream().map(Element::getName).collect(Collectors.joining(",")));
        String attrs = "linha,cidade,mensagem,email,empresaID,nomeFantasia,sistemaViarioUrbano,razaoSocial,cnpj,endereco,telefone,funcionario,numViagens,terminalID,nomeTerminal,validadores,numViagens,horaEntrada,tipoViagem,passagem,nivelMaximoAcesso,funcionario,cargoID,nomeCargo,hora,pagamento,tipoPassageiro,viagem,corrida,linha,tempoMaxIntegracao,tempoID,desconto,nomeTipo,tipoID,descricaoTipo,formaPagamentoPassagem,tipopassageiro,linhaMgr,onibusID,validador,onibusID,validadorID,tempoDecorrido,numViagem,tempoDecorrido,porcentagemDesconto,politicaID,tipoID,limitePassagens,terminal,quantidadePassagensMes,dataInicioContagem,numCartoesID,limiteCartoes,viacaoNumViagens,politicaDesconto,valorTarifa,dataAtualizacao,tarifaID,nomeTarifa,telefone,dataNascimento,instituicao,cartao,nomePassageiro,passageiroID,rg,email,endereco,cpf,estadoCivil,cartaoLimitePassagens,tipoPassageiroLimitePassagens,saida,horaSaidaPrevista,horaChegadaPrevista,horaSaida,horaChegada,arrecadacao,corridaID,numCorrida,tipoPssageiroPagamentoCartao,pagamentoPagamentoCartao,viagem,pontoSaida,meioDePassagem,linhaID,nomeLinha,pontoChegada,linha,corrida,email,empresaID,nomeFantasia,endereco,telefone,contato,passageiro,cnpj,razaoSocial,valorPagamento,dataPagamento,pagamentoID,tipoPagamento,pagamentoID,travada,dados,gerenciaTipoPassageiroPagamentoCartao,gerenciaPagamentoPagamentoCartao,nivelMaximoAcesso,tipoPassageiro,dataValidade,cartaoID,viagem,pagamento,dataAquisicao,saldo,nomeMenuArquivo,numViagens,numViagensID,limiteViagensIntegracao,data,empresaViaria,sistemaViarioUrbano,corridaDados,valorAquisicao,tipoID,pagamentoAquisicaoCartao,viacaoIntegracaoTempo,login,usuarioID,senhaAcesso,nivelAcesso,funcionarioID,endereco,telefone,nomeFuncionario,dataAdimissao,cargo,funcionario,numViagem,meioDePassagem,visor,catraca,leitoraCartao,chegadaPrevista,saidaPrevista,corridaID,numCorridaDIa,saida,horaChegada,horaSaida,viacaoNumCartoes,data,interfaceCartaoMgt";
        assertEquals(attrs, architecture.getAllAtributtes().stream().map(Element::getName).collect(Collectors.joining(",")));
        String classes = "SistemaViarioUrbano,GerenciaSistViarioNumCartoesTempoNumViagens,Visor,EmpresaViaria,GerenciaCargo,AcessoBasicoLimPassagensEmpresaUsuaria,GerenciaViagem,ViagemIntegracaoCtrl,ViagemNumViagens,EmpresaUsuariaMgr,AcessoBasicoLimitePassagens,Autenticacao,GerenciaPssageiroEmpresaUsuaria,Terminal,GerenciaCartaoNumCartoes,ViagemNumViagensCtrl,ValidadorOnibusCtrl,CargaCartaoLimPassagensEmpresaUsuaria,CombinacaoMgr,ViagemIntegracao,GerenciaIntegracaoCombinacaoLimPassagensCtrl,AquisicaoCartaoCombinacao,ControladorBET,Cargo,Viagem,AquisicaoCartao,CartaoMgr,LinhaMgr,NumeroViagensCtrl,TempoViagemCtrl,AcessoBasicoEmpresaUsuaria,ViacaoIntegracaoTempo,TipoPassageiroDAOCombinarCartoes,TipoPassageiro,Onibus,ValidadorMgr,EmpresaUsuariaDAO,ViagemCtrlTempoNumViagens,ViagemCtrlTempo,GerenciaPassageiro,PoliticaDesconto,TipoPassageiroLimitePassagens,TerminalMgr,CartaoLimitePassagens,CargaCartaoEmpresaUsuaria,ViacaoNumCartoes,GerenciaLinhaIntegrada,GerenciaPagamento,GerenciaTarefa,NumViagensMgr,GerenciaCartao,Tarifa,MeioDePassagem,Passageiro,LimitePassagensMgr,Corrida,ViagemLinhaIntegradaCtrl,PagamentoCartaoMgr,GerenciaCartaoLimPassagens,Linha,GerenciaSistViarioTempoNumViagens,EmpresaUsuaria,ViacaoTempoMgr,GerenciaFuncionario,GerenciaTerminal,GerenciaValidador,GerenciaEmpresaUsuaria,Pagamento,PagamentoPagamentoCartao,AcessoAdicional,Catraca,LinhaIntegradaMgr,LeitoraCartao,GerenciaOnibus,GerenciaTipoPassageiroLimPassagens,GerenciaSistViarioNumCartoes,GerenciaSistViarioUrbano,CartaoPagamentoCartaoCtrl,ViagemCtrl,GerenciaCartaoPagamentoCartao,GerenciaEmpresaViaria,GerenciaLinha,ControladorBETWeb,Cartao,Autorizacao,GerenciaCorrida,ViacaoNumViagensMgr,ViacaoNumViagens,AcessoBasico,ViagemTempoNumViagensLinhaIntegradaCtrl,ViacaoMgr,CorridaCtrl,LinhaIntegradaViagemCtrl,TipoPssageiroPagamentoCartao,ViagemTempoLinhaIntegradaCtrl,ViagemNumViagensLinhaIntegradaCtrl,TempoMgr,GerenciaTipoPassageiro,Usuario,Funcionario,FuncionarioMgr,GerenciaTipoPassageiroPagamentoCartao,GerenciaPagamentoPagamentoCartao,ValidadorServidorCtrl,GerenciaCtrl,ViagemTempoCtrl,ViagemCtrlNumViagens,Validador,CorridaDados,GerenciaSistViarioTempo,NumCartoesMgr,CargaCartao,ViagemTempoNumViagensCtrl,GerenciaTipoPassageiroCombinarCartoes,CargaCartaoLimitePassagens";
        assertEquals(classes, architecture.getAllClasses().stream().map(Element::getName).collect(Collectors.joining(",")));
        String interfaces = "IViacaoMgt,ISolicitarCartao,IValidadorMgt,ITerminalMgt,IObterTempo,ILimitePassagensMgt,ICartaoMgt,IRegistrarViagem,IObterValorPassagem,IRegistrarArrecadacao,ICombinacaoMgt,ITratarCartao,ILinhaMgt,INumViagensMgt,IPermitirLeitura,ILinhaIntegradaMgt,ITarifaMgt,IFuncionarioMgt,ISolicitarGerencia,ITempoMgt,IProcessarTransacao,INumCartoesMgt,IPagamentoCartaoMgt,IAtualizarCorrida,ISolicitarCarga,IProcessarViagem,IObterNumViagens,IRegistrarCorrida,IEmpresaUsuariaMgt,IPassageiroMgt";
        assertEquals(interfaces, architecture.getAllInterfaces().stream().map(Element::getName).collect(Collectors.joining(",")));
        String variationPoints = "TipoPassageiro,Passageiro,Viagem,SistemaViarioUrbano,TipoPassageiro,Pagamento";
        assertEquals(variationPoints, architecture.getAllVariationPoints().stream().map(variationPoint -> variationPoint.getVariationPointElement().getName()).collect(Collectors.joining(",")));
        String variabilities = "LimitePassagensTipoPassageiro,ResponsaveisCartao,AcessoAdicional,LinhaDeIntegracao,PagamentoEmCartaoTipoPassageiro,PagamentoCartaoCartao";
        assertEquals(variabilities, architecture.getAllVariabilities().stream().map(Variability::getName).collect(Collectors.joining(",")));
        String variants = "TipoPassageiroLimitePassagens,Passageiro,EmpresaUsuaria,ViagemIntegracao,ViagemNumViagens,ViacaoNumViagens,ViacaoIntegracaoTempo,PagamentoPagamentoCartao";
        assertEquals(variants, architecture.getAllVariants().stream().map(Variant::getName).collect(Collectors.joining(",")));
    }

    @Test
    public void buildMM1onSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        String xmiFilePath = agm + Constants.FILE_SEPARATOR + "mm1.smty";
        Architecture architecture = new ArchitectureBuilderSMarty().create(xmiFilePath);

        assertEquals(11, architecture.getAllPackages().size());
        assertEquals(17, architecture.getAllClasses().size());
        assertEquals(18, architecture.getAllInterfaces().size());
        assertEquals(2, architecture.getAllVariabilities().size());
        assertEquals(6, architecture.getAllVariants().size());
        assertEquals(2, architecture.getAllVariationPoints().size());
        assertEquals(14, architecture.getAllAtributtes().size());
        assertEquals(164, architecture.getAllMethods().size());

        String methods = "albumctrl,managealbum,addmediaalbum,getmedia,showmedias,aretheremedia,ismediaused,islabelused,hasmemory,aretherefavouritemedia,ismediafavourite,islabeled,managefavouritemedia,linkmedia,managemedia,hearmedia,labelmedia,playmedia,sendmedia,aretherephotos,showphotos,getmedia,linkmediaentry,showentries,arethereentries,checkpassword,checklogin,managefavouritemedia,sendmedia,playmedia,hearmedia,linkmedia,managemedia,getmedia,addmedia,setlabel,setfavourite,showmedia,copymedia,removemedia,aretherevideos,showvideos,getentry,hasmediaassociated,showentry,linkmediaentry,hasalbumthismedia,getalbum,addalbum,showalbum,isalbumempty,removealbum,addmediaalbum,getmedia,showmusics,aretheremusics,addmediaalbum,managealbum,getuser,addmediaalbum,managealbum,sendmediaemail,getnumber,getemail,sendmediasms,aretherefavouritemedia,addmedia,copymedia,showmedia,sendmediaemail,aretheremedia,ismediafavourite,islabelused,ismediaused,islabeled,sendmediasms,setfavourite,setlabel,hasmemory,removemedia,getmedia,getentry,showentries,arethereentries,hasmediaassociated,linkmediaentry,ismusicused,login,aretherephotos,createmusic,showvideos,showphotos,showmusics,isphotoused,deletevideo,deletephoto,aretherevideos,aretheremusics,deletemusic,createvideo,createphoto,hasmemory,playmusic,visualizephoto,hasmediaassociated,visualizephoto,playmusic,playvideo,isphotoused,addphoto,aretherephotos,showphotos,getphoto,removephoto,showentries,arethereentries,linkmediaentry,ismediassociated,islabelused,setlabel,islabelblank,islabeled,setfavourite,aretrerefavouritemedia,ismediafavourite,showfavouritemedias,getuser,showvideos,aretherevideos,getvideo,removevideo,addvideo,playmedia,hearmedia,managemedia,linkmedia,managefavouritemedia,sendmedia,arethesealbuns,hasalbumthismedia,addmediaalbum,showalbuns,copymedia,arethesealbuns,getalbum,isalbumempty,hasalbumthismedia,removealbum,addmediaalbum,showalbuns,isnameused,addalbum,createalbum,deletealbum,isalbumempty,isnameused,showalbuns,arethesealbuns,removemusic,aretheremusics,showmusics,getmusic,ismusicused,addmusic";
        assertEquals(methods, architecture.getAllMethods().stream().map(Element::getName).collect(Collectors.joining(",")));
        String attrs = "videofile,photofile,password,login,label,name,numberofexecutions,favourite,size,number,name,media,name,musicfile";
        assertEquals(attrs, architecture.getAllAtributtes().stream().map(Element::getName).collect(Collectors.joining(",")));
        String classes = "AlbumCtrl,Video,MediaMgr,MediaCtrl,PhotMgr,Photo,EntryMgr,User,MediaGUI,Media,VideoMgr,Entry,Album,Music,MusicMgr,AlbumGUI,UserMgr";
        assertEquals(classes, architecture.getAllClasses().stream().map(Element::getName).collect(Collectors.joining(",")));
        String interfaces = "IAlbumGUI,ISendMedia,IMediaMgt,IEntryMgt,IManageMedia,IHearMedia,IPlayMedia,IPhotoMgt,ILinkMedia,ILabelFiles,IManageFavouriteMedia,IUserMgt,IVideoMgt,IMediaGUI,IAddMediaAlbum,IAlbumMgt,IManageAlbum,IMusicMgt";
        assertEquals(interfaces, architecture.getAllInterfaces().stream().map(Element::getName).collect(Collectors.joining(",")));
        String variationPoints = "Media,Media";
        assertEquals(variationPoints, architecture.getAllVariationPoints().stream().map(variationPoint -> variationPoint.getVariationPointElement().getName()).collect(Collectors.joining(",")));
        String variabilities = "ManaingMedia,ViewPlayMedia";
        assertEquals(variabilities, architecture.getAllVariabilities().stream().map(Variability::getName).collect(Collectors.joining(",")));
        String variants = "Photo,Music,Video,Photo,Music,Video";
        assertEquals(variants, architecture.getAllVariants().stream().map(Variant::getName).collect(Collectors.joining(",")));
    }

    @Test
    public void buildMM2onSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        String xmiFilePath = agm + Constants.FILE_SEPARATOR + "mm2.smty";
        Architecture architecture = new ArchitectureBuilderSMarty().create(xmiFilePath);

        assertEquals(8, architecture.getAllPackages().size());
        assertEquals(14, architecture.getAllClasses().size());
        assertEquals(15, architecture.getAllInterfaces().size());
        assertEquals(2, architecture.getAllVariabilities().size());
        assertEquals(6, architecture.getAllVariants().size());
        assertEquals(2, architecture.getAllVariationPoints().size());
        assertEquals(13, architecture.getAllAtributtes().size());
        assertEquals(147, architecture.getAllMethods().size());

        String methods = "albumctrl,managealbum,addmediaalbum,aretheremusics,aretheremedia,showvideos,hasmemory,ismediafavourite,islabeled,showmusics,showmedias,showphotos,ismediaused,islabelused,aretherefavouritemedia,aretherevideos,aretherephotos,managefavouritemedia,linkmedia,managemedia,hearmedia,labelmedia,playmedia,sendmedia,getmedia,linkmediaentry,showentries,arethereentries,checkpassword,checklogin,managefavouritemedia,sendmedia,playmedia,hearmedia,linkmedia,managemedia,getmedia,addmedia,setlabel,setfavourite,showmedia,copymedia,removemedia,getentry,hasmediaassociated,showentry,linkmediaentry,hasalbumthismedia,getalbum,addalbum,showalbum,isalbumempty,removealbum,addmediaalbum,getmedia,addmediaalbum,managealbum,getmedia,getuser,addmediaalbum,managealbum,sendmediaemail,getnumber,getemail,sendmediasms,aretherefavouritemedia,addmedia,copymedia,showmedia,sendmediaemail,aretheremedia,ismediafavourite,islabelused,ismediaused,islabeled,sendmediasms,setfavourite,setlabel,hasmemory,removemedia,getmedia,getentry,showentries,arethereentries,hasmediaassociated,linkmediaentry,ismusicused,login,aretherephotos,createmusic,showvideos,showphotos,showmusics,isphotoused,deletevideo,deletephoto,aretherevideos,aretheremusics,deletemusic,createvideo,createphoto,hasmemory,playmusic,visualizephoto,hasmediaassociated,visualizephoto,playmusic,playvideo,showentries,arethereentries,linkmediaentry,ismediassociated,islabelused,setlabel,islabelblank,islabeled,setfavourite,aretrerefavouritemedia,ismediafavourite,showfavouritemedias,getuser,playmedia,hearmedia,managemedia,linkmedia,managefavouritemedia,sendmedia,arethesealbuns,hasalbumthismedia,addmediaalbum,showalbuns,copymedia,arethesealbuns,getalbum,isalbumempty,hasalbumthismedia,removealbum,addmediaalbum,showalbuns,isnameused,addalbum,createalbum,deletealbum,isalbumempty,isnameused,showalbuns,arethesealbuns";
        assertEquals(methods, architecture.getAllMethods().stream().map(Element::getName).collect(Collectors.joining(",")));
        String attrs = "musicfile,password,login,label,name,numberofexecutions,favourite,size,number,name,name,photofile,videofile";
        assertEquals(attrs, architecture.getAllAtributtes().stream().map(Element::getName).collect(Collectors.joining(",")));
        String classes = "AlbumCtrl,MediaMgr,MediaCtrl,Music,EntryMgr,User,MediaGUI,Media,Entry,Album,Photo,AlbumGUI,Video,UserMgr";
        assertEquals(classes, architecture.getAllClasses().stream().map(Element::getName).collect(Collectors.joining(",")));
        String interfaces = "IAlbumGUI,ISendMedia,IMediaMgt,IEntryMgt,IManageMedia,IHearMedia,IPlayMedia,ILinkMedia,ILabelFiles,IManageFavouriteMedia,IUserMgt,IMediaGUI,IAddMediaAlbum,IAlbumMgt,IManageAlbum";
        assertEquals(interfaces, architecture.getAllInterfaces().stream().map(Element::getName).collect(Collectors.joining(",")));
        String variationPoints = "Media,Media";
        assertEquals(variationPoints, architecture.getAllVariationPoints().stream().map(variationPoint -> variationPoint.getVariationPointElement().getName()).collect(Collectors.joining(",")));
        String variabilities = "ManaingMedia,ViewPlayMedia";
        assertEquals(variabilities, architecture.getAllVariabilities().stream().map(Variability::getName).collect(Collectors.joining(",")));
        String variants = "Photo,Music,Video,Photo,Music,Video";
        assertEquals(variants, architecture.getAllVariants().stream().map(Variant::getName).collect(Collectors.joining(",")));
    }

    @Test
    public void buildMMAtualonSmarty() throws Exception {
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAConfigThreadScope.setConfig(applicationYamlConfig);
        String xmiFilePath = agm + Constants.FILE_SEPARATOR + "MMAtual.smty";
        Architecture architecture = new ArchitectureBuilderSMarty().create(xmiFilePath);

        assertEquals(8, architecture.getAllPackages().size());
        assertEquals(14, architecture.getAllClasses().size());
        assertEquals(15, architecture.getAllInterfaces().size());
        assertEquals(2, architecture.getAllVariabilities().size());
        assertEquals(6, architecture.getAllVariants().size());
        assertEquals(2, architecture.getAllVariationPoints().size());
        assertEquals(19, architecture.getAllAtributtes().size());
        assertEquals(146, architecture.getAllMethods().size());

        String methods = "addMediaAlbum,manageAlbum,isLabeled,isMediaUsed,isMediaFavourite,isLabelUsed,areThereMusics,showMusics,showMedias,showPhotos,showVideos,areThereMedia,areThereVideos,areTherePhotos,areThereFavouriteMedia,hasMemory,sendMedia,labelMedia,playMedia,hearMedia,linkMedia,manageMedia,manageFavouriteMedia,getMedia,linkMediaEntry,areThereEntries,showEntries,checkPassword,checkLogin,sendMedia,linkMedia,manageFavouriteMedia,manageMedia,hearMedia,playMedia,showMedia,addMedia,removeMedia,setFavourite,copyMedia,setLabel,getMedia,showEntry,hasMediaAssociated,getEntry,linkMediaEntry,showAlbum,hasAlbumThisMedia,addMediaAlbum,addAlbum,removeAlbum,isAlbumEmpty,getAlbum,getMedia,manageAlbum,addMediaAlbum,getMedia,getUser,addMediaAlbum,manageAlbum,sendMediaSMS,getEmail,sendMediaEmail,getNumber,isLabelUsed,hasMemory,isMediaUsed,isMediaFavourite,areThereFavouriteMedia,removeMedia,setLabel,addMedia,sendMediaSMS,setFavourite,showMedia,getMedia,copyMedia,isLabeled,sendMediaEmail,areThereMedia,hasMediaAssociated,showEntries,linkMediaEntry,getEntry,areThereEntries,showPhotos,createMusic,areTherePhotos,createVideo,logIn,showMusics,isPhotoUsed,showVideos,isMusicUsed,createPhoto,hasMemory,deleteMusic,deleteVideo,deletePhoto,areThereVideos,areThereMusics,visualizePhoto,playMusic,hasMediaAssociated,playMusic,visualizePhoto,playVideo,areThereEntries,showEntries,isMediaAssociated,linkMediaEntry,isLabelUsed,isLabeled,isLabelBlank,setLabel,setFavourite,showFavouriteMedias,areThereFavouriteMedias,isMediaFavourite,getUser,manageFavouriteMedia,playMedia,hearMedia,manageMedia,sendMedia,linkMedia,areThereAlbuns,addMediaAlbum,copyMedia,showAlbuns,hasAlbumThisMedia,areThereAlbuns,isAlbumEmpty,showAlbuns,getAlbum,addMediaAlbum,addAlbum,isNameUsed,hasAlbumThisMedia,removeAlbum,showAlbuns,isAlbumEmpty,createAlbum,isNameUsed,areThereAlbuns,deleteAlbum";
        assertEquals(methods, architecture.getAllMethods().stream().map(Element::getName).collect(Collectors.joining(",")));
        String attrs = "media,musicFile,entry,password,login,media,entry,label,name,favourite,size,numberOfExecutions,number,name,media,name,photoFile,videoFile,user";
        assertEquals(attrs, architecture.getAllAtributtes().stream().map(Element::getName).collect(Collectors.joining(",")));
        String classes = "AlbumCtrl,MediaMgr,MediaCtrl,Music,EntryMgr,User,MediaGUI,Media,Entry,Album,Photo,AlbumGUI,Video,UserMgr";
        assertEquals(classes, architecture.getAllClasses().stream().map(Element::getName).collect(Collectors.joining(",")));
        String interfaces = "IAlbumGUI,ISendMedia,IMediaMgt,IEntryMgt,IManageMedia,IHearMedia,IPlayMedia,ILinkMedia,ILabelFiles,IManageFavouriteMedia,IUserMgt,IMediaGUI,IAddMediaAlbum,IAlbumMgt,IManageAlbum";
        assertEquals(interfaces, architecture.getAllInterfaces().stream().map(Element::getName).collect(Collectors.joining(",")));
        String variationPoints = "Media,Media";
        assertEquals(variationPoints, architecture.getAllVariationPoints().stream().map(variationPoint -> variationPoint.getVariationPointElement().getName()).collect(Collectors.joining(",")));
        String variabilities = "ManagingMedia,ViewPlayMedia";
        assertEquals(variabilities, architecture.getAllVariabilities().stream().map(Variability::getName).collect(Collectors.joining(",")));
        String variants = "Photo,Music,Video,Photo,Video,Music";
        assertEquals(variants, architecture.getAllVariants().stream().map(Variant::getName).collect(Collectors.joining(",")));
    }

}
