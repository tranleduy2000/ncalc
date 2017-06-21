/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.casiofx.factory;

import com.duy.casiofx.token.ConstantToken;

/**
 * A Factory that produces specific Mathematical and Physical Constants.
 *
 * @author Ejaaz Merali
 * @version 3.0
 */
public class ConstantFactory {

    public static ConstantToken makeSpeedOfLight() {
        return new ConstantToken("Speed of Light", "c", "c", 299792458, "m·s<sup><small>-1</small></sup>") {
            public String toLaTeX() {
                return "c";
            }
        };
    }

    public static ConstantToken makePlanck() {
        return new ConstantToken("Planck constant", "h", "h", 6.62606957e-34, "J·s") {
            public String toLaTeX() {
                return "h";
            }
        };
    }

    public static ConstantToken makeRedPlanck() {
        return new ConstantToken("Reduced Planck constant", "ħ", "ħ", 1.054571726e-34, "J·s") {
            public String toLaTeX() {
                return "ħ";
            }
        };
    }

    public static ConstantToken makeGravitational() {
        return new ConstantToken("Gravitational constant", "G", "G", 6.67384e-11, "N·m<sup><small>2</small></sup>·kg<sup><small>-2</small></sup>") {
            public String toLaTeX() {
                return "G";
            }
        };
    }

    public static ConstantToken makeGasConst() {
        return new ConstantToken("Molar gas constant", "R", "R", 8.3144621, "J·mol<sup><small>-1</small></sup>·K<sup><small>-1</small></sup>") {
            public String toLaTeX() {
                return "R";
            }
        };
    }

    public static ConstantToken makeBoltzmann() {
        return new ConstantToken(
                "Boltzmann's constant",
                "k<sub><small><small><small>B</small></small></small></sub>",
                "k☺B☺",
                1.3806488e-23,
                "J·K<sup><small>-1</small></sup>") {
            public String toLaTeX() {
                return "k_{B}";
            }
        };
    }

    public static ConstantToken makeAvogadro() {
        return new ConstantToken(
                "Avogadro's Number",
                "N<sub><small><small><small>A</small></small></small></sub>",
                "N☺A☺",
                6.02214129e23,
                "mol<sup><small>-1</small></sup>") {
            public String toLaTeX() {
                return "N_{A}";
            }
        };
    }

    public static ConstantToken makeStefanBoltzmann() {
        return new ConstantToken("Stefan-Boltzmann constant", "σ", "σ", 5.670373e-8, "W·m<sup><small>-2</small></sup>·K<sup><small>-4</small></sup>") {
            public String toLaTeX() {
                return "\\sigma";
            }
        };
    }

    public static ConstantToken makeFaraday() {
        return new ConstantToken("Faraday constant", "F", "F", 96485.3365, "C·mol<sup><small>-1</small></sup>") {
            public String toLaTeX() {
                return "F";
            }
        };
    }

    public static ConstantToken makeMagnetic() {
        return new ConstantToken(
                "Vacuum Permeability",
                "μ<sub><small><small><small>0</small></small></small></sub>",
                "μ☺0☺",
                Math.PI * (4e-7),
                "N·A<sup><small>-2</small></sup>") {
            public String toLaTeX() {
                return "\\mu_{0}";
            }
        };
    }

    public static ConstantToken makeElectric() {
        return new ConstantToken("Vacuum Permittivity",
                "ɛ<sub><small><small><small>0</small></small></small></sub>",
                "ɛ☺0☺",
                8.854187817e-12,
                "C·V<sup><small>-1</small></sup>·m<sup><small>-1</small></sup>") {
            public String toLaTeX() {
                return "\\epsilon_{0}";
            }
        };
    }

    public static ConstantToken makeCoulomb() {
        return new ConstantToken("Coulomb's Constant", "k", "k", 8.987551787e9, "N·m<sup><small>2</small></sup>·C<sup><small>-2</small></sup>") {
            public String toLaTeX() {
                return "k_{e}";
            }
        };
    }

    public static ConstantToken makeElemCharge() {
        return new ConstantToken("Elementary Charge", "e", "e", 1.602176565e-19, "C") {
            public String toLaTeX() {
                return "e";
            }
        };
    }

    public static ConstantToken makeElectronVolt() {
        return new ConstantToken("Electronvolt", "eV", "eV", 1.602176565e-19, "J") {
            public String toLaTeX() {
                return "eV";
            }
        };
    }

    public static ConstantToken makeElectronMass() {
        return new ConstantToken("Electron Mass",
                "m<sub><small><small><small>e</small></small></small></sub>",
                "m☺e☺",
                9.10938291e-31,
                "kg") {
            public String toLaTeX() {
                return "m_{e}";
            }
        };
    }

    public static ConstantToken makeProtonMass() {
        return new ConstantToken("Proton Mass",
                "m<sub><small><small><small>p</small></small></small></sub>",
                "m☺p☺",
                1.672621777e-27,
                "kg") {
            public String toLaTeX() {
                return "m_p";
            }
        };
    }

    public static ConstantToken makeNeutronMass() {
        return new ConstantToken("Neutron Mass",
                "m<sub><small><small><small>n</small></small></small></sub>",
                "m☺n☺",
                1.674927351e-27,
                "kg") {
            public String toLaTeX() {
                return "m_n";
            }
        };
    }

    public static ConstantToken makeAtomicMass() {
        return new ConstantToken("Atomic Mass constant", "u", "u", 1.660538921e-27, "kg") {

            public String toLaTeX() {
                return "u";
            }
        };
    }

    public static ConstantToken makeBohrMagneton() {
        return new ConstantToken(
                "Bohr magneton",
                "μ<sub><small><small><small>B</small></small></small></sub>",
                "μ☺B☺",
                9.27400968e-24,
                "J·T<sup><small>-1</small></sup>") {
            public String toLaTeX() {
                return "μ_{B}";
            }
        };
    }

    public static ConstantToken makeBohrRadius() {
        return new ConstantToken(
                "Bohr radius",
                "a<sub><small><small><small>0</small></small></small></sub>",
                "a☺0☺",
                5.2917721092e-11, "m") {
            public String toLaTeX() {
                return "a_{0}";
            }
        };
    }

    public static ConstantToken makeRydberg() {
        return new ConstantToken("Rydberg constant",
                "R<sub><small><small><small>∞</small></small></small></sub>",
                "R☺∞☺",
                10973731.568539, "m<sup><small>-1</small></sup>") {

            public String toLaTeX() {
                return "R_{\\infty}";
            }
        };
    }

    public static ConstantToken makeFineStruct() {
        return new ConstantToken("Fine-structure constant", "α", "α", 7.2973525698e-3, "") {

            public String toLaTeX() {
                return "\\alpha";
            }
        };
    }

    public static ConstantToken makeMagneticFluxQuantum() {
        return new ConstantToken(
                "Magnetic flux quantum",
                "Φ<sub><small><small><small>0</small></small></small></sub>",
                "Φ☺0☺",
                2.067833758e-15, "Wb") {
            public String toLaTeX() {
                return "\\phi_{0}";
            }
        };
    }

    public static ConstantToken makeEarthGrav() {
        return new ConstantToken("Surface gravity of Earth",
                "g<sub><small><small><small>⊕</small></small></small></sub>",
                "g☺⊕☺",
                9.80665, "m·s<sup><small>-2</small></sup>") {

            public String toLaTeX() {
                return "g_{\\Earth}";
            }
        };
    }

    public static ConstantToken makeEarthMass() {
        return new ConstantToken("Mass of Earth",
                "M<sub><small><small><small>⊕</small></small></small></sub>",
                "M☺⊕☺",
                5.97219e24, "kg") {
            public String toLaTeX() {
                return "M_{\\Earth}";
            }
        };
    }

    public static ConstantToken makeEarthRadius() {
        return new ConstantToken("Mean radius of Earth",
                "R<sub><small><small><small>⊕</small></small></small></sub>",
                "R☺⊕☺",
                6371009, "m") {

            public String toLaTeX() {
                return "R_{\\Earth}";
            }
        };
    }

    public static ConstantToken makeSolarMass() {
        return new ConstantToken("Mass of the Sun",
                "M<sub><small><small><small>⊙</small></small></small></sub>",
                "M☺⊙☺",
                1.98855e30, "kg") {

            public String toLaTeX() {
                return "M_{\\Sun}";
            }
        };
    }

    public static ConstantToken makeSolarRadius() {
        return new ConstantToken("Radius of the Sun",
                "R<sub><small><small><small>⊙</small></small></small></sub>",
                "R☺⊙☺",
                6.955e8, "m") {
            public String toLaTeX() {
                return "R_{\\Sun}";
            }
        };
    }

    public static ConstantToken makeSolarLuminosity() {
        return new ConstantToken("Luminosity of the Sun",
                "L<sub><small><small><small>⊙</small></small></small></sub>",
                "L☺⊙☺",
                3.846e26, "W") {
            public String toLaTeX() {
                return "L_{\\Sun";
            }
        };
    }

    public static ConstantToken makeAU() {
        return new ConstantToken("Astronomical Unit", "AU", "AU", 1.495978707e11, "m") {

            public String toLaTeX() {
                return "AU";
            }
        };
    }

    public static ConstantToken makeLightYear() {
        return new ConstantToken("Light Year", "ly", "ly", 9.4607304725808e15, "m") {

            public String toLaTeX() {
                return "ly";
            }
        };
    }

    public static ConstantToken makePhi() {
        return new ConstantToken("The Golden Ratio", "ϕ", "ϕ", 1.61803398874, "") {

            public String toLaTeX() {
                return "\\phi";
            }
        };
    }

    public static ConstantToken makeEulerMascheroni() {
        return new ConstantToken("Euler-Mascheroni constant", "γ", "γ", 0.577215664901532860606512, "") {

            public String toLaTeX() {
                return "\\gamma";
            }
        };
    }
}
