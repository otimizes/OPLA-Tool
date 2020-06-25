'use strict';


customElements.define('compodoc-menu', class extends HTMLElement {
    constructor() {
        super();
        this.isNormalMode = this.getAttribute('mode') === 'normal';
    }

    connectedCallback() {
        this.render(this.isNormalMode);
    }

    render(isNormalMode) {
        let tp = lithtml.html(`
        <nav>
            <ul class="list">
                <li class="title">
                    <a href="index.html" data-type="index-link">OPLA-Front Documentation</a>
                </li>

                <li class="divider"></li>
                ${ isNormalMode ? `<div id="book-search-input" role="search"><input type="text" placeholder="Type to search"></div>` : '' }
                <li class="chapter">
                    <a data-type="chapter-link" href="index.html"><span class="icon ion-ios-home"></span>Getting started</a>
                    <ul class="links">
                        <li class="link">
                            <a href="overview.html" data-type="chapter-link">
                                <span class="icon ion-ios-keypad"></span>Overview
                            </a>
                        </li>
                        <li class="link">
                            <a href="index.html" data-type="chapter-link">
                                <span class="icon ion-ios-paper"></span>README
                            </a>
                        </li>
                                <li class="link">
                                    <a href="dependencies.html" data-type="chapter-link">
                                        <span class="icon ion-ios-list"></span>Dependencies
                                    </a>
                                </li>
                    </ul>
                </li>
                    <li class="chapter modules">
                        <a data-type="chapter-link" href="modules.html">
                            <div class="menu-toggler linked" data-toggle="collapse" ${ isNormalMode ?
                                'data-target="#modules-links"' : 'data-target="#xs-modules-links"' }>
                                <span class="icon ion-ios-archive"></span>
                                <span class="link-name">Modules</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                        </a>
                        <ul class="links collapse " ${ isNormalMode ? 'id="modules-links"' : 'id="xs-modules-links"' }>
                            <li class="link">
                                <a href="modules/AppModule.html" data-type="entity-link">AppModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#components-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' : 'data-target="#xs-components-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' :
                                            'id="xs-components-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' }>
                                            <li class="link">
                                                <a href="components/AppComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">AppComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/DialogTooltipInfo.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">DialogTooltipInfo</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ExecutionComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ExecutionComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ExperimentsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ExperimentsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/InteractionDialogComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">InteractionDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/LoginComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">LoginComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/LogsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">LogsComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/OplaComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">OplaComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/PapyrusSettingsDialog.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">PapyrusSettingsDialog</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/PatternComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">PatternComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ResultsComponent.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ResultsComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                                <li class="chapter inner">
                                    <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                        'data-target="#directives-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' : 'data-target="#xs-directives-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' }>
                                        <span class="icon ion-md-code-working"></span>
                                        <span>Directives</span>
                                        <span class="icon ion-ios-arrow-down"></span>
                                    </div>
                                    <ul class="links collapse" ${ isNormalMode ? 'id="directives-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' :
                                        'id="xs-directives-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' }>
                                        <li class="link">
                                            <a href="directives/DragDropDirectiveDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">DragDropDirectiveDirective</a>
                                        </li>
                                        <li class="link">
                                            <a href="directives/NgVarDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">NgVarDirective</a>
                                        </li>
                                        <li class="link">
                                            <a href="directives/OplaI18nDirective.html"
                                                data-type="entity-link" data-context="sub-entity" data-context-id="modules">OplaI18nDirective</a>
                                        </li>
                                    </ul>
                                </li>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ?
                                            'data-target="#pipes-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' : 'data-target="#xs-pipes-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' }>
                                            <span class="icon ion-md-add"></span>
                                            <span>Pipes</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="pipes-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' :
                                            'id="xs-pipes-links-module-AppModule-9b5a478f0af9f49fbebb7b5c240661fd"' }>
                                            <li class="link">
                                                <a href="pipes/ReplaceallPipe.html"
                                                    data-type="entity-link" data-context="sub-entity" data-context-id="modules">ReplaceallPipe</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/AppRoutingModule.html" data-type="entity-link">AppRoutingModule</a>
                            </li>
                </ul>
                </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#classes-links"' :
                            'data-target="#xs-classes-links"' }>
                            <span class="icon ion-ios-paper"></span>
                            <span>Classes</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="classes-links"' : 'id="xs-classes-links"' }>
                            <li class="link">
                                <a href="classes/AppPage.html" data-type="entity-link">AppPage</a>
                            </li>
                            <li class="link">
                                <a href="classes/Config.html" data-type="entity-link">Config</a>
                            </li>
                            <li class="link">
                                <a href="classes/OptimizationDto.html" data-type="entity-link">OptimizationDto</a>
                            </li>
                            <li class="link">
                                <a href="classes/OptimizationInfo.html" data-type="entity-link">OptimizationInfo</a>
                            </li>
                            <li class="link">
                                <a href="classes/PersistenceService.html" data-type="entity-link">PersistenceService</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#injectables-links"' :
                                'data-target="#xs-injectables-links"' }>
                                <span class="icon ion-md-arrow-round-down"></span>
                                <span>Injectables</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                            <ul class="links collapse " ${ isNormalMode ? 'id="injectables-links"' : 'id="xs-injectables-links"' }>
                                <li class="link">
                                    <a href="injectables/AlertService.html" data-type="entity-link">AlertService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/CmObjectiveFunctionService.html" data-type="entity-link">CmObjectiveFunctionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/CsObjectiveFunctionService.html" data-type="entity-link">CsObjectiveFunctionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/DistanceEuclideanService.html" data-type="entity-link">DistanceEuclideanService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ElegObjectiveFunctionService.html" data-type="entity-link">ElegObjectiveFunctionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ExecutionService.html" data-type="entity-link">ExecutionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ExperimentConfigurationService.html" data-type="entity-link">ExperimentConfigurationService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ExperimentService.html" data-type="entity-link">ExperimentService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ExtObjectiveFunctionService.html" data-type="entity-link">ExtObjectiveFunctionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/InfoService.html" data-type="entity-link">InfoService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/MapObjectiveNameService.html" data-type="entity-link">MapObjectiveNameService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/ObjectiveService.html" data-type="entity-link">ObjectiveService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/OptimizationService.html" data-type="entity-link">OptimizationService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/RccObjectiveFunctionService.html" data-type="entity-link">RccObjectiveFunctionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/SdObjectiveFunctionService.html" data-type="entity-link">SdObjectiveFunctionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/SvObjectiveFunctionService.html" data-type="entity-link">SvObjectiveFunctionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/TvObjectiveFunctionService.html" data-type="entity-link">TvObjectiveFunctionService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/UserService.html" data-type="entity-link">UserService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/WocsClassObjectiveFunctionService.html" data-type="entity-link">WocsClassObjectiveFunctionService</a>
                                </li>
                            </ul>
                        </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#guards-links"' :
                            'data-target="#xs-guards-links"' }>
                            <span class="icon ion-ios-lock"></span>
                            <span>Guards</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="guards-links"' : 'id="xs-guards-links"' }>
                            <li class="link">
                                <a href="guards/AppGuardComponent.html" data-type="entity-link">AppGuardComponent</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-toggle="collapse" ${ isNormalMode ? 'data-target="#miscellaneous-links"'
                            : 'data-target="#xs-miscellaneous-links"' }>
                            <span class="icon ion-ios-cube"></span>
                            <span>Miscellaneous</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="miscellaneous-links"' : 'id="xs-miscellaneous-links"' }>
                            <li class="link">
                                <a href="miscellaneous/variables.html" data-type="entity-link">Variables</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <a data-type="chapter-link" href="routes.html"><span class="icon ion-ios-git-branch"></span>Routes</a>
                        </li>
                    <li class="chapter">
                        <a data-type="chapter-link" href="coverage.html"><span class="icon ion-ios-stats"></span>Documentation coverage</a>
                    </li>
                    <li class="divider"></li>
                    <li class="copyright">
                        Documentation generated using <a href="https://compodoc.app/" target="_blank">
                            <img data-src="images/compodoc-vectorise-inverted.png" class="img-responsive" data-type="compodoc-logo">
                        </a>
                    </li>
            </ul>
        </nav>
        `);
        this.innerHTML = tp.strings;
    }
});